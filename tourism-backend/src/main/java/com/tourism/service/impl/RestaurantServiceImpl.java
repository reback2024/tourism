package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.dto.RestaurantQueryDTO;
import com.tourism.entity.Restaurant;
import com.tourism.mapper.RestaurantMapper;
import com.tourism.security.SecurityUtils;
import com.tourism.service.RestaurantService;
import com.tourism.vo.RestaurantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantMapper mapper;
    private static final Set<String> ALLOWED_SORT = Set.of("rating", "price_per_person", "review_count");

    public RestaurantServiceImpl(RestaurantMapper mapper) { this.mapper = mapper; }

    @Override
    public IPage<RestaurantVO> listRestaurants(Integer page, Integer size, RestaurantQueryDTO query) {
        LambdaQueryWrapper<Restaurant> w = new LambdaQueryWrapper<>();
        w.eq(Restaurant::getStatus, 1);

        if (StringUtils.hasText(query.getKeyword())) {
            w.and(wr -> wr.like(Restaurant::getName, query.getKeyword()).or().like(Restaurant::getDescription, query.getKeyword()));
        }
        if (StringUtils.hasText(query.getCity())) w.eq(Restaurant::getCity, query.getCity());
        if (StringUtils.hasText(query.getCuisineType())) w.eq(Restaurant::getCuisineType, query.getCuisineType());

        String sortBy = query.getSortBy();
        if (StringUtils.hasText(sortBy) && ALLOWED_SORT.contains(sortBy)) {
            if ("price_per_person".equals(sortBy)) w.orderBy(true, true, Restaurant::getPricePerPerson);
            else if ("review_count".equals(sortBy)) w.orderBy(true, false, Restaurant::getReviewCount);
            else w.orderBy(true, false, Restaurant::getRating);
        } else {
            w.orderByDesc(Restaurant::getRating);
        }

        return mapper.selectPage(new Page<>(page, size), w).convert(this::toVO);
    }

    @Override
    public RestaurantVO getRestaurantById(Long id) {
        Restaurant r = mapper.selectById(id);
        if (r == null || r.getStatus() == 0) return null;
        RestaurantVO vo = toVO(r);
        if (SecurityUtils.isAuthenticated()) vo.setIsFavorited(false);
        return vo;
    }

    @Override
    public List<String> getCities() {
        return mapper.selectList(
            new LambdaQueryWrapper<Restaurant>().select(Restaurant::getCity).eq(Restaurant::getStatus, 1).groupBy(Restaurant::getCity)
        ).stream().map(Restaurant::getCity).distinct().collect(Collectors.toList());
    }

    private RestaurantVO toVO(Restaurant r) {
        RestaurantVO vo = new RestaurantVO();
        BeanUtils.copyProperties(r, vo);
        return vo;
    }
}
