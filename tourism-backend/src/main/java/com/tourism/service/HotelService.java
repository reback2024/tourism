package com.tourism.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.dto.HotelQueryDTO;
import com.tourism.vo.HotelVO;

import java.util.List;

public interface HotelService {
    IPage<HotelVO> listHotels(Integer page, Integer size, HotelQueryDTO query);
    HotelVO getHotelById(Long id);
    List<String> getCities();
}
