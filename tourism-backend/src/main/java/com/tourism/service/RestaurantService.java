package com.tourism.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.dto.RestaurantQueryDTO;
import com.tourism.vo.RestaurantVO;
import java.util.List;

public interface RestaurantService {
    IPage<RestaurantVO> listRestaurants(Integer page, Integer size, RestaurantQueryDTO query);
    RestaurantVO getRestaurantById(Long id);
    List<String> getCities();
}
