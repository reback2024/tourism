package com.tourism.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.dto.ReviewCreateDTO;
import com.tourism.vo.ReviewVO;

public interface ReviewService {
    IPage<ReviewVO> getReviews(String targetType, Long targetId, Integer page, Integer size);
    ReviewVO createReview(Long userId, ReviewCreateDTO dto);
    void deleteReview(Long userId, Long reviewId);
    IPage<ReviewVO> getMyReviews(Long userId, Integer page, Integer size);
}
