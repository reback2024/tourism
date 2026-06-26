package com.tourism.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.dto.FavoriteDTO;
import com.tourism.security.SecurityUtils;
import com.tourism.service.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    public FavoriteController(FavoriteService favoriteService) { this.favoriteService = favoriteService; }

    @PostMapping
    public Result<Void> add(@Valid @RequestBody FavoriteDTO dto) {
        favoriteService.addFavorite(SecurityUtils.getCurrentUserId(), dto.getTargetType(), dto.getTargetId());
        return Result.ok("收藏成功", null);
    }

    @DeleteMapping
    public Result<Void> remove(@Valid @RequestBody FavoriteDTO dto) {
        favoriteService.removeFavorite(SecurityUtils.getCurrentUserId(), dto.getTargetType(), dto.getTargetId());
        return Result.ok("已取消收藏", null);
    }

    @GetMapping
    public Result<PageResult<?>> list(@RequestParam(required = false) String targetType,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        IPage<?> result = favoriteService.getMyFavorites(SecurityUtils.getCurrentUserId(), targetType, page, size);
        return Result.ok(PageResult.of(result));
    }

    @GetMapping("/check")
    public Result<List<Long>> check(@RequestParam String targetType, @RequestParam List<Long> targetIds) {
        List<Long> ids = favoriteService.checkFavorites(SecurityUtils.getCurrentUserId(), targetType, targetIds);
        return Result.ok(ids);
    }
}
