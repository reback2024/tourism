package com.tourism.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.entity.Review;
import com.tourism.entity.User;
import com.tourism.mapper.ReviewMapper;
import com.tourism.mapper.UserMapper;
import com.tourism.vo.ReviewVO;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final ReviewMapper mapper;
    private final UserMapper userMapper;
    public AdminReviewController(ReviewMapper m, UserMapper u) { this.mapper = m; this.userMapper = u; }

    @GetMapping
    public Result<PageResult<Map<String,Object>>> list(@RequestParam(defaultValue="1") Integer page,
                                                        @RequestParam(defaultValue="10") Integer size) {
        IPage<Review> p = mapper.selectPage(new Page<>(page, size),
            new LambdaQueryWrapper<Review>().orderByDesc(Review::getCreateTime));
        List<Map<String,Object>> records = new ArrayList<>();
        for (Review r : p.getRecords()) {
            Map<String,Object> m = new HashMap<>();
            m.put("id", r.getId()); m.put("userId", r.getUserId()); m.put("targetType", r.getTargetType());
            m.put("targetId", r.getTargetId()); m.put("rating", r.getRating()); m.put("content", r.getContent());
            m.put("status", r.getStatus()); m.put("createTime", r.getCreateTime());
            User u = userMapper.selectById(r.getUserId());
            m.put("username", u != null ? (u.getNickname() != null ? u.getNickname() : u.getUsername()) : "");
            records.add(m);
        }
        return Result.ok(PageResult.of(p.getTotal(), p.getCurrent(), p.getSize(), records));
    }

    @PutMapping("/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestBody Map<String,Integer> body) {
        Review r = mapper.selectById(id);
        if (r != null) { r.setStatus(body.getOrDefault("status", 1)); mapper.updateById(r); }
        return Result.ok("审核完成", null);
    }
}
