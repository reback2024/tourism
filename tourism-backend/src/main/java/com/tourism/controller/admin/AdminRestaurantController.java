package com.tourism.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.entity.Restaurant;
import com.tourism.mapper.RestaurantMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {

    private final RestaurantMapper mapper;
    public AdminRestaurantController(RestaurantMapper m) { this.mapper = m; }

    @GetMapping
    public Result<PageResult<Restaurant>> list(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Restaurant> w = new LambdaQueryWrapper<>();
        if (keyword != null) w.like(Restaurant::getName, keyword);
        w.orderByDesc(Restaurant::getCreateTime);
        return Result.ok(PageResult.of(mapper.selectPage(new Page<>(page, size), w)));
    }

    @PostMapping
    public Result<Void> create(@RequestBody Restaurant r) {
        r.setId(null); r.setRating(java.math.BigDecimal.ZERO); r.setReviewCount(0);
        mapper.insert(r); return Result.ok("添加成功", null);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Restaurant r) {
        Restaurant exist = mapper.selectById(id);
        if (exist != null) {
            BeanUtils.copyProperties(r, exist, "id", "rating", "reviewCount", "createTime", "updateTime");
            mapper.updateById(exist);
        }
        return Result.ok("更新成功", null);
    }

    @DeleteMapping("/{id}") public Result<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return Result.ok("删除成功", null); }
    @PutMapping("/{id}/status") public Result<Void> toggle(@PathVariable Long id) {
        Restaurant r = mapper.selectById(id); if (r != null) { r.setStatus(r.getStatus()==1?0:1); mapper.updateById(r); }
        return Result.ok("上下架成功", null);
    }
}
