package com.tourism.controller;

import com.tourism.common.Result;
import com.tourism.dto.ItineraryCreateDTO;
import com.tourism.security.SecurityUtils;
import com.tourism.service.ItineraryService;
import com.tourism.vo.ItineraryVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {

    private final ItineraryService service;
    public ItineraryController(ItineraryService service) { this.service = service; }

    @PostMapping
    public Result<ItineraryVO> create(@Valid @RequestBody ItineraryCreateDTO dto) {
        return Result.ok("行程创建成功", service.createItinerary(SecurityUtils.getCurrentUserId(), dto));
    }

    @GetMapping
    public Result<List<ItineraryVO>> myList() {
        return Result.ok(service.getUserItineraries(SecurityUtils.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public Result<ItineraryVO> detail(@PathVariable Long id) {
        ItineraryVO vo = service.getItineraryById(id);
        if (vo == null) return Result.fail(404, "行程不存在");
        return Result.ok(vo);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.deleteItinerary(SecurityUtils.getCurrentUserId(), id);
        return Result.ok("删除成功", null);
    }
}
