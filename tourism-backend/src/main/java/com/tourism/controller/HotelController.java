package com.tourism.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.dto.HotelQueryDTO;
import com.tourism.service.HotelService;
import com.tourism.vo.HotelVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;
    public HotelController(HotelService hotelService) { this.hotelService = hotelService; }

    @GetMapping
    public Result<PageResult<HotelVO>> list(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             HotelQueryDTO query) {
        IPage<HotelVO> result = hotelService.listHotels(page, size, query);
        return Result.ok(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<HotelVO> detail(@PathVariable Long id) {
        HotelVO vo = hotelService.getHotelById(id);
        if (vo == null) return Result.fail(404, "酒店不存在或已下架");
        return Result.ok(vo);
    }

    @GetMapping("/cities")
    public Result<List<String>> cities() {
        return Result.ok(hotelService.getCities());
    }
}
