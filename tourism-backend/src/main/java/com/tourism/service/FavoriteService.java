package com.tourism.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.vo.AttractionVO;
import com.tourism.vo.HotelVO;
import com.tourism.vo.RestaurantVO;

import java.util.List;

public interface FavoriteService {
    void addFavorite(Long userId, String targetType, Long targetId);
    void removeFavorite(Long userId, String targetType, Long targetId);
    List<Long> checkFavorites(Long userId, String targetType, List<Long> targetIds);
    IPage<?> getMyFavorites(Long userId, String targetType, Integer page, Integer size);
    boolean isFavorited(Long userId, String targetType, Long targetId);
}
