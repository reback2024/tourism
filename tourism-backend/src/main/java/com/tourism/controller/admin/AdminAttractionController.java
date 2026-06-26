package com.tourism.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.entity.Attraction;
import com.tourism.mapper.AttractionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/attractions")
public class AdminAttractionController {

    private final AttractionMapper mapper;
    public AdminAttractionController(AttractionMapper m) { this.mapper = m; }

    @GetMapping
    public Result<PageResult<Attraction>> list(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Attraction> w = new LambdaQueryWrapper<>();
        if (keyword != null) w.like(Attraction::getName, keyword);
        w.orderByDesc(Attraction::getCreateTime);
        IPage<Attraction> p = mapper.selectPage(new Page<>(page, size), w);
        return Result.ok(PageResult.of(p));
    }

    @PostMapping
    public Result<Void> create(@RequestBody Attraction attraction) {
        attraction.setId(null); attraction.setRating(java.math.BigDecimal.ZERO); attraction.setReviewCount(0);
        mapper.insert(attraction); return Result.ok("添加成功", null);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Attraction attraction) {
        Attraction exist = mapper.selectById(id);
        if (exist != null) {
            BeanUtils.copyProperties(attraction, exist, "id", "rating", "reviewCount", "createTime", "updateTime");
            mapper.updateById(exist);
        }
        return Result.ok("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return Result.ok("删除成功", null); }

    @PutMapping("/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        Attraction a = mapper.selectById(id);
        if (a != null) { a.setStatus(a.getStatus() == 1 ? 0 : 1); mapper.updateById(a); }
        return Result.ok("上下架成功", null);
    }
}
