package com.tourism.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.dto.AttractionQueryDTO;
import com.tourism.vo.AttractionVO;

import java.util.List;

public interface AttractionService {

    /** 分页列表（含筛选和排序） */
    IPage<AttractionVO> listAttractions(Integer page, Integer size, AttractionQueryDTO query);

    /** 详情 */
    AttractionVO getAttractionById(Long id);

    /** 热门Top10 */
    List<AttractionVO> getHotAttractions();

    /** 城市列表 */
    List<String> getCities();
}
