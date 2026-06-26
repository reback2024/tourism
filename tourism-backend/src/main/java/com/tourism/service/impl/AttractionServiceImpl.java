package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.dto.AttractionQueryDTO;
import com.tourism.entity.Attraction;
import com.tourism.mapper.AttractionMapper;
import com.tourism.security.SecurityUtils;
import com.tourism.service.AttractionService;
import com.tourism.vo.AttractionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttractionServiceImpl implements AttractionService {

    private final AttractionMapper attractionMapper;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("rating", "create_time", "review_count");

    public AttractionServiceImpl(AttractionMapper attractionMapper) {
        this.attractionMapper = attractionMapper;
    }

    @Override
    public IPage<AttractionVO> listAttractions(Integer page, Integer size, AttractionQueryDTO query) {
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 1);

        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w
                    .like(Attraction::getName, query.getKeyword())
                    .or()
                    .like(Attraction::getDescription, query.getKeyword()));
        }

        if (StringUtils.hasText(query.getCity())) {
            wrapper.eq(Attraction::getCity, query.getCity());
        }

        if (StringUtils.hasText(query.getCategory())) {
            wrapper.eq(Attraction::getCategory, query.getCategory());
        }

        String sortBy = query.getSortBy();
        if (StringUtils.hasText(sortBy) && ALLOWED_SORT_FIELDS.contains(sortBy)) {
            boolean isAsc = "asc".equalsIgnoreCase(query.getOrder());
            if ("rating".equals(sortBy)) {
                wrapper.orderBy(true, isAsc, Attraction::getRating);
            } else if ("review_count".equals(sortBy)) {
                wrapper.orderBy(true, isAsc, Attraction::getReviewCount);
            } else {
                wrapper.orderBy(true, isAsc, Attraction::getCreateTime);
            }
        } else {
            wrapper.orderByDesc(Attraction::getRating);
        }

        IPage<Attraction> entityPage = attractionMapper.selectPage(new Page<>(page, size), wrapper);
        return entityPage.convert(this::toVO);
    }

    @Override
    public AttractionVO getAttractionById(Long id) {
        Attraction attraction = attractionMapper.selectById(id);
        if (attraction == null || attraction.getStatus() == 0) {
            return null;
        }
        AttractionVO vo = toVO(attraction);
        if (SecurityUtils.isAuthenticated()) {
            vo.setIsFavorited(false);
        }
        return vo;
    }

    @Override
    public List<AttractionVO> getHotAttractions() {
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attraction::getStatus, 1)
                .orderByDesc(Attraction::getRating)
                .orderByDesc(Attraction::getReviewCount)
                .last("LIMIT 10");
        return attractionMapper.selectList(wrapper).stream()
                .map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public List<String> getCities() {
        LambdaQueryWrapper<Attraction> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Attraction::getCity)
                .eq(Attraction::getStatus, 1)
                .groupBy(Attraction::getCity);
        return attractionMapper.selectList(wrapper).stream()
                .map(Attraction::getCity).distinct().collect(Collectors.toList());
    }

    private AttractionVO toVO(Attraction entity) {
        AttractionVO vo = new AttractionVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
