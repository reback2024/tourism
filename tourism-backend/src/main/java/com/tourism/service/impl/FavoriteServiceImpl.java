package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.common.BusinessException;
import com.tourism.common.ResultCode;
import com.tourism.entity.*;
import com.tourism.mapper.*;
import com.tourism.service.FavoriteService;
import com.tourism.vo.AttractionVO;
import com.tourism.vo.HotelVO;
import com.tourism.vo.RestaurantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final AttractionMapper attractionMapper;
    private final HotelMapper hotelMapper;
    private final RestaurantMapper restaurantMapper;

    public FavoriteServiceImpl(FavoriteMapper favoriteMapper, AttractionMapper aMapper,
                                HotelMapper hMapper, RestaurantMapper rMapper) {
        this.favoriteMapper = favoriteMapper;
        this.attractionMapper = aMapper;
        this.hotelMapper = hMapper;
        this.restaurantMapper = rMapper;
    }

    @Override
    public void addFavorite(Long userId, String targetType, Long targetId) {
        Long count = favoriteMapper.selectCount(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId));
        if (count > 0) throw new BusinessException(ResultCode.DUPLICATE_FAVORITE);

        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setTargetType(targetType);
        fav.setTargetId(targetId);
        favoriteMapper.insert(fav);
    }

    @Override
    public void removeFavorite(Long userId, String targetType, Long targetId) {
        favoriteMapper.delete(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId));
    }

    @Override
    public List<Long> checkFavorites(Long userId, String targetType, List<Long> targetIds) {
        return favoriteMapper.selectList(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .in(Favorite::getTargetId, targetIds))
            .stream().map(Favorite::getTargetId).collect(Collectors.toList());
    }

    @Override
    public IPage<?> getMyFavorites(Long userId, String targetType, Integer page, Integer size) {
        LambdaQueryWrapper<Favorite> w = new LambdaQueryWrapper<>();
        w.eq(Favorite::getUserId, userId)
         .eq(targetType != null, Favorite::getTargetType, targetType)
         .orderByDesc(Favorite::getCreateTime);

        IPage<Favorite> favPage = favoriteMapper.selectPage(new Page<>(page, size), w);
        return favPage.convert(fav -> {
            return switch (fav.getTargetType()) {
                case "ATTRACTION" -> {
                    Attraction a = attractionMapper.selectById(fav.getTargetId());
                    AttractionVO vo = new AttractionVO();
                    if (a != null) BeanUtils.copyProperties(a, vo);
                    yield vo;
                }
                case "HOTEL" -> {
                    Hotel h = hotelMapper.selectById(fav.getTargetId());
                    HotelVO vo = new HotelVO();
                    if (h != null) BeanUtils.copyProperties(h, vo);
                    yield vo;
                }
                case "RESTAURANT" -> {
                    Restaurant r = restaurantMapper.selectById(fav.getTargetId());
                    RestaurantVO vo = new RestaurantVO();
                    if (r != null) BeanUtils.copyProperties(r, vo);
                    yield vo;
                }
                default -> null;
            };
        });
    }

    @Override
    public boolean isFavorited(Long userId, String targetType, Long targetId) {
        return favoriteMapper.selectCount(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getTargetType, targetType)
                .eq(Favorite::getTargetId, targetId)) > 0;
    }
}
