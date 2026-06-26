package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tourism.common.BusinessException;
import com.tourism.dto.ItineraryCreateDTO;
import com.tourism.entity.Itinerary;
import com.tourism.entity.ItineraryDay;
import com.tourism.mapper.ItineraryDayMapper;
import com.tourism.mapper.ItineraryMapper;
import com.tourism.service.ItineraryService;
import com.tourism.vo.ItineraryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryMapper mapper;
    private final ItineraryDayMapper dayMapper;

    public ItineraryServiceImpl(ItineraryMapper mapper, ItineraryDayMapper dayMapper) {
        this.mapper = mapper; this.dayMapper = dayMapper;
    }

    @Override
    @Transactional
    public ItineraryVO createItinerary(Long userId, ItineraryCreateDTO dto) {
        Itinerary itinerary = new Itinerary();
        BeanUtils.copyProperties(dto, itinerary, "daysDetail");
        itinerary.setUserId(userId); itinerary.setStatus(1);
        mapper.insert(itinerary);

        if (dto.getDaysDetail() != null) {
            for (ItineraryCreateDTO.DayDTO day : dto.getDaysDetail()) {
                ItineraryDay d = new ItineraryDay();
                d.setItineraryId(itinerary.getId());
                d.setDayNumber(day.getDayNumber());
                d.setAttractions(day.getAttractions());
                d.setHotels(day.getHotels());
                d.setRestaurants(day.getRestaurants());
                d.setNotes(day.getNotes());
                dayMapper.insert(d);
            }
        }

        return toVO(itinerary);
    }

    @Override
    public List<ItineraryVO> getUserItineraries(Long userId) {
        return mapper.selectList(
            new LambdaQueryWrapper<Itinerary>().eq(Itinerary::getUserId, userId).eq(Itinerary::getStatus, 1).orderByDesc(Itinerary::getCreateTime)
        ).stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public ItineraryVO getItineraryById(Long id) {
        Itinerary i = mapper.selectById(id);
        if (i == null || i.getStatus() != 1) return null;
        return toVO(i);
    }

    @Override
    public void deleteItinerary(Long userId, Long id) {
        Itinerary i = mapper.selectById(id);
        if (i == null || !i.getUserId().equals(userId)) throw new BusinessException("无权删除");
        i.setStatus(0); mapper.updateById(i);
    }

    private ItineraryVO toVO(Itinerary i) {
        ItineraryVO vo = new ItineraryVO(); BeanUtils.copyProperties(i, vo);
        List<ItineraryDay> days = dayMapper.selectList(
            new LambdaQueryWrapper<ItineraryDay>().eq(ItineraryDay::getItineraryId, i.getId()).orderByAsc(ItineraryDay::getDayNumber));
        vo.setDaysDetail(days.stream().map(d -> { ItineraryVO.DayVO dv = new ItineraryVO.DayVO(); BeanUtils.copyProperties(d, dv); return dv; }).collect(Collectors.toList()));
        return vo;
    }
}
