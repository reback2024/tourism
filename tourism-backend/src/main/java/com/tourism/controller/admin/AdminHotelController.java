package com.tourism.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.entity.Hotel;
import com.tourism.mapper.HotelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/hotels")
public class AdminHotelController {

    private final HotelMapper mapper;
    public AdminHotelController(HotelMapper m) { this.mapper = m; }

    @GetMapping
    public Result<PageResult<Hotel>> list(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Hotel> w = new LambdaQueryWrapper<>();
        if (keyword != null) w.like(Hotel::getName, keyword);
        w.orderByDesc(Hotel::getCreateTime);
        return Result.ok(PageResult.of(mapper.selectPage(new Page<>(page, size), w)));
    }

    @PostMapping
    public Result<Void> create(@RequestBody Hotel hotel) {
        hotel.setId(null); hotel.setRating(java.math.BigDecimal.ZERO); hotel.setReviewCount(0);
        mapper.insert(hotel); return Result.ok("添加成功", null);
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Hotel h) {
        Hotel exist = mapper.selectById(id);
        if (exist != null) {
            BeanUtils.copyProperties(h, exist, "id", "rating", "reviewCount", "createTime", "updateTime");
            mapper.updateById(exist);
        }
        return Result.ok("更新成功", null);
    }

    @DeleteMapping("/{id}") public Result<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return Result.ok("删除成功", null); }

    @PutMapping("/{id}/status") public Result<Void> toggle(@PathVariable Long id) {
        Hotel h = mapper.selectById(id); if (h != null) { h.setStatus(h.getStatus()==1?0:1); mapper.updateById(h); }
        return Result.ok("上下架成功", null);
    }
}
