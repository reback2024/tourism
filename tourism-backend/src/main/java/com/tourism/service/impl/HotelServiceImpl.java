package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.dto.HotelQueryDTO;
import com.tourism.entity.Hotel;
import com.tourism.mapper.HotelMapper;
import com.tourism.security.SecurityUtils;
import com.tourism.service.HotelService;
import com.tourism.vo.HotelVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelMapper hotelMapper;
    private static final Set<String> ALLOWED_SORT = Set.of("rating", "star_level", "price_min", "review_count");

    public HotelServiceImpl(HotelMapper hotelMapper) { this.hotelMapper = hotelMapper; }

    @Override
    public IPage<HotelVO> listHotels(Integer page, Integer size, HotelQueryDTO query) {
        LambdaQueryWrapper<Hotel> w = new LambdaQueryWrapper<>();
        w.eq(Hotel::getStatus, 1);

        if (StringUtils.hasText(query.getKeyword())) {
            w.and(wr -> wr.like(Hotel::getName, query.getKeyword()).or().like(Hotel::getDescription, query.getKeyword()));
        }
        if (StringUtils.hasText(query.getCity())) {
            w.eq(Hotel::getCity, query.getCity());
        }
        if (query.getStarLevel() != null) {
            w.eq(Hotel::getStarLevel, query.getStarLevel());
        }
        if (query.getPriceMin() != null) {
            w.ge(Hotel::getPriceMin, query.getPriceMin());
        }
        if (query.getPriceMax() != null) {
            w.le(Hotel::getPriceMax, query.getPriceMax());
        }

        String sortBy = query.getSortBy();
        boolean asc = false;
        if (StringUtils.hasText(sortBy) && ALLOWED_SORT.contains(sortBy)) {
            if ("star_level".equals(sortBy)) w.orderBy(true, asc, Hotel::getStarLevel);
            else if ("price_min".equals(sortBy)) w.orderBy(true, asc, Hotel::getPriceMin);
            else if ("review_count".equals(sortBy)) w.orderBy(true, false, Hotel::getReviewCount);
            else w.orderBy(true, false, Hotel::getRating);
        } else {
            w.orderByDesc(Hotel::getRating);
        }

        return hotelMapper.selectPage(new Page<>(page, size), w).convert(this::toVO);
    }

    @Override
    public HotelVO getHotelById(Long id) {
        Hotel h = hotelMapper.selectById(id);
        if (h == null || h.getStatus() == 0) return null;
        HotelVO vo = toVO(h);
        if (SecurityUtils.isAuthenticated()) vo.setIsFavorited(false);
        return vo;
    }

    @Override
    public List<String> getCities() {
        return hotelMapper.selectList(
            new LambdaQueryWrapper<Hotel>().select(Hotel::getCity).eq(Hotel::getStatus, 1).groupBy(Hotel::getCity)
        ).stream().map(Hotel::getCity).distinct().collect(Collectors.toList());
    }

    private HotelVO toVO(Hotel h) {
        HotelVO vo = new HotelVO();
        BeanUtils.copyProperties(h, vo);
        return vo;
    }
}
