package com.tourism.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.dto.ReviewCreateDTO;
import com.tourism.security.SecurityUtils;
import com.tourism.service.ReviewService;
import com.tourism.vo.ReviewVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) { this.reviewService = reviewService; }

    @GetMapping
    public Result<PageResult<ReviewVO>> list(@RequestParam String targetType,
                                              @RequestParam Long targetId,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        IPage<ReviewVO> result = reviewService.getReviews(targetType, targetId, page, size);
        return Result.ok(PageResult.of(result));
    }

    @PostMapping
    public Result<ReviewVO> create(@Valid @RequestBody ReviewCreateDTO dto) {
        Long userId = SecurityUtils.getCurrentUserId();
        return Result.ok("评价发表成功", reviewService.createReview(userId, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        reviewService.deleteReview(SecurityUtils.getCurrentUserId(), id);
        return Result.ok("评价已删除", null);
    }

    @GetMapping("/my")
    public Result<PageResult<ReviewVO>> myReviews(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        IPage<ReviewVO> result = reviewService.getMyReviews(SecurityUtils.getCurrentUserId(), page, size);
        return Result.ok(PageResult.of(result));
    }
}
