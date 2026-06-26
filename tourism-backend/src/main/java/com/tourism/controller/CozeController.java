package com.tourism.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.common.Result;
import com.tourism.entity.SysConfig;
import com.tourism.mapper.SysConfigMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coze")
public class CozeController {

    private final SysConfigMapper configMapper;
    public CozeController(SysConfigMapper configMapper) { this.configMapper = configMapper; }

    @GetMapping("/config")
    public Result<Map<String, String>> getConfig() {
        List<SysConfig> configs = configMapper.selectList(
            new LambdaQueryWrapper<SysConfig>().like(SysConfig::getConfigKey, "coze"));
        Map<String, String> map = new HashMap<>();
        for (SysConfig c : configs) {
            map.put(c.getConfigKey(), c.getConfigValue());
        }
        map.putIfAbsent("coze_bot_enabled", "false");
        return Result.ok(map);
    }
}
