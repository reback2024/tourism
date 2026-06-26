package com.tourism.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.dto.AttractionQueryDTO;
import com.tourism.service.AttractionService;
import com.tourism.vo.AttractionVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
public class AttractionController {

    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @GetMapping
    public Result<PageResult<AttractionVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            AttractionQueryDTO query) {
        IPage<AttractionVO> result = attractionService.listAttractions(page, size, query);
        return Result.ok(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<AttractionVO> detail(@PathVariable Long id) {
        AttractionVO vo = attractionService.getAttractionById(id);
        if (vo == null) {
            return Result.fail(404, "景点不存在或已下架");
        }
        return Result.ok(vo);
    }

    @GetMapping("/hot")
    public Result<List<AttractionVO>> hot() {
        return Result.ok(attractionService.getHotAttractions());
    }

    @GetMapping("/cities")
    public Result<List<String>> cities() {
        return Result.ok(attractionService.getCities());
    }
}
