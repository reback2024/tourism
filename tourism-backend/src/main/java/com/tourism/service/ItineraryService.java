package com.tourism.service;

import com.tourism.dto.ItineraryCreateDTO;
import com.tourism.vo.ItineraryVO;
import java.util.List;

public interface ItineraryService {
    ItineraryVO createItinerary(Long userId, ItineraryCreateDTO dto);
    List<ItineraryVO> getUserItineraries(Long userId);
    ItineraryVO getItineraryById(Long id);
    void deleteItinerary(Long userId, Long id);
}
