package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.common.BusinessException;
import com.tourism.common.ResultCode;
import com.tourism.dto.ReviewCreateDTO;
import com.tourism.entity.Attraction;
import com.tourism.entity.Hotel;
import com.tourism.entity.Restaurant;
import com.tourism.entity.Review;
import com.tourism.entity.User;
import com.tourism.mapper.*;
import com.tourism.service.ReviewService;
import com.tourism.vo.ReviewVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;
    private final AttractionMapper attractionMapper;
    private final HotelMapper hotelMapper;
    private final RestaurantMapper restaurantMapper;

    public ReviewServiceImpl(ReviewMapper reviewMapper, UserMapper userMapper,
                              AttractionMapper attractionMapper, HotelMapper hotelMapper,
                              RestaurantMapper restaurantMapper) {
        this.reviewMapper = reviewMapper;
        this.userMapper = userMapper;
        this.attractionMapper = attractionMapper;
        this.hotelMapper = hotelMapper;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public IPage<ReviewVO> getReviews(String targetType, Long targetId, Integer page, Integer size) {
        LambdaQueryWrapper<Review> w = new LambdaQueryWrapper<>();
        w.eq(Review::getTargetType, targetType)
         .eq(Review::getTargetId, targetId)
         .eq(Review::getStatus, 1)
         .orderByDesc(Review::getCreateTime);

        IPage<Review> result = reviewMapper.selectPage(new Page<>(page, size), w);
        return result.convert(this::toVO);
    }

    @Override
    @Transactional
    public ReviewVO createReview(Long userId, ReviewCreateDTO dto) {
        // 检查重复评价
        Long count = reviewMapper.selectCount(
            new LambdaQueryWrapper<Review>()
                .eq(Review::getUserId, userId)
                .eq(Review::getTargetType, dto.getTargetType())
                .eq(Review::getTargetId, dto.getTargetId())
                .eq(Review::getStatus, 1));
        if (count > 0) {
            throw new BusinessException(ResultCode.DUPLICATE_REVIEW);
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setTargetType(dto.getTargetType());
        review.setTargetId(dto.getTargetId());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setImages(dto.getImages());
        review.setStatus(1);
        reviewMapper.insert(review);

        // 更新目标评分
        updateTargetRating(dto.getTargetType(), dto.getTargetId());

        return toVO(review);
    }

    @Override
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null || !review.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此评价");
        }
        review.setStatus(0);
        reviewMapper.updateById(review);
        updateTargetRating(review.getTargetType(), review.getTargetId());
    }

    @Override
    public IPage<ReviewVO> getMyReviews(Long userId, Integer page, Integer size) {
        LambdaQueryWrapper<Review> w = new LambdaQueryWrapper<>();
        w.eq(Review::getUserId, userId).eq(Review::getStatus, 1).orderByDesc(Review::getCreateTime);
        return reviewMapper.selectPage(new Page<>(page, size), w).convert(this::toVO);
    }

    private void updateTargetRating(String targetType, Long targetId) {
        // 计算平均评分
        LambdaQueryWrapper<Review> w = new LambdaQueryWrapper<>();
        w.eq(Review::getTargetType, targetType).eq(Review::getTargetId, targetId).eq(Review::getStatus, 1);
        Long totalCount = reviewMapper.selectCount(w);

        var reviews = reviewMapper.selectList(w);
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0);
        BigDecimal rating = BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP);

        switch (targetType) {
            case "ATTRACTION" -> {
                Attraction a = attractionMapper.selectById(targetId);
                if (a != null) { a.setRating(rating); a.setReviewCount(totalCount.intValue()); attractionMapper.updateById(a); }
            }
            case "HOTEL" -> {
                Hotel h = hotelMapper.selectById(targetId);
                if (h != null) { h.setRating(rating); h.setReviewCount(totalCount.intValue()); hotelMapper.updateById(h); }
            }
            case "RESTAURANT" -> {
                Restaurant r = restaurantMapper.selectById(targetId);
                if (r != null) { r.setRating(rating); r.setReviewCount(totalCount.intValue()); restaurantMapper.updateById(r); }
            }
        }
    }

    private ReviewVO toVO(Review r) {
        ReviewVO vo = new ReviewVO();
        BeanUtils.copyProperties(r, vo);
        User user = userMapper.selectById(r.getUserId());
        if (user != null) {
            vo.setUsername(user.getNickname() != null ? user.getNickname() : user.getUsername());
            vo.setUserAvatar(user.getAvatar());
        }
        return vo;
    }
}
