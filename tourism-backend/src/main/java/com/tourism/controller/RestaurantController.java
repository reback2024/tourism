package com.tourism.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.dto.RestaurantQueryDTO;
import com.tourism.service.RestaurantService;
import com.tourism.vo.RestaurantVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService service;
    public RestaurantController(RestaurantService service) { this.service = service; }

    @GetMapping
    public Result<PageResult<RestaurantVO>> list(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   RestaurantQueryDTO query) {
        IPage<RestaurantVO> result = service.listRestaurants(page, size, query);
        return Result.ok(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<RestaurantVO> detail(@PathVariable Long id) {
        RestaurantVO vo = service.getRestaurantById(id);
        if (vo == null) return Result.fail(404, "餐厅不存在或已下架");
        return Result.ok(vo);
    }

    @GetMapping("/cities")
    public Result<List<String>> cities() { return Result.ok(service.getCities()); }
}
