package com.tourism.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.entity.SysConfig;
import com.tourism.mapper.SysConfigMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ZhipuAiService {

    private final SysConfigMapper configMapper;
    private final RestTemplate restTemplate;
    private static final String API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private static final String MODEL = "glm-4-flash";

    public ZhipuAiService(SysConfigMapper configMapper, RestTemplate restTemplate) {
        this.configMapper = configMapper;
        this.restTemplate = restTemplate;
    }

    /** 获取API Key */
    private String getApiKey() {
        SysConfig config = configMapper.selectOne(
            new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, "zhipu_api_key"));
        return config != null ? config.getConfigValue() : "";
    }

    /** 通用对话 */
    public String chat(String userMessage, List<Map<String, String>> history) {
        return callApi(buildMessages(userMessage, history, null));
    }

    /** 生成行程 */
    public String generateItinerary(String destination, int days, double budget, String preferences) {
        String systemPrompt = "你是专业旅游规划师。请简洁地输出行程，每项用一两句话即可。无需长篇介绍。";

        String userPrompt = String.format(
            "请帮我规划一个%s%d日游。总预算约%.0f元。我的兴趣偏好：%s。请给出详细的每日行程。",
            destination, days, budget, preferences != null && !preferences.isEmpty() ? preferences : "综合体验");

        return callApi(buildMessages(userPrompt, null, systemPrompt));
    }

    private String callApi(JSONObject requestBody) {
        try {
            String apiKey = getApiKey();
            if (apiKey.isEmpty()) return "❌ 请先在系统配置中设置智谱AI API Key";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<String> entity = new HttpEntity<>(requestBody.toJSONString(), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JSONObject result = JSON.parseObject(response.getBody());
                JSONArray choices = result.getJSONArray("choices");
                if (choices != null && !choices.isEmpty()) {
                    return choices.getJSONObject(0).getJSONObject("message").getString("content");
                }
            }
            return "AI服务暂时不可用，请稍后重试";
        } catch (Exception e) {
            return "AI服务异常: " + e.getMessage();
        }
    }

    private JSONObject buildMessages(String userMsg, List<Map<String, String>> history, String systemPrompt) {
        JSONObject body = new JSONObject();
        body.put("model", MODEL);

        JSONArray messages = new JSONArray();

        if (systemPrompt != null) {
            JSONObject sys = new JSONObject();
            sys.put("role", "system");
            sys.put("content", systemPrompt);
            messages.add(sys);
        }

        if (history != null) {
            for (Map<String, String> msg : history) {
                JSONObject m = new JSONObject();
                m.put("role", msg.get("role"));
                m.put("content", msg.get("content"));
                messages.add(m);
            }
        }

        JSONObject user = new JSONObject();
        user.put("role", "user");
        user.put("content", userMsg);
        messages.add(user);

        body.put("messages", messages);
        body.put("temperature", 0.6);
        body.put("max_tokens", 800);

        return body;
    }
}
