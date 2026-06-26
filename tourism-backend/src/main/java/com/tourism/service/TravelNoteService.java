package com.tourism.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.dto.TravelNoteCreateDTO;
import com.tourism.vo.TravelNoteVO;
import java.util.List;

public interface TravelNoteService {
    IPage<TravelNoteVO> listNotes(Integer page, Integer size, String keyword, String destination, String sortBy);
    TravelNoteVO getNoteById(Long id);
    TravelNoteVO createNote(Long userId, TravelNoteCreateDTO dto);
    TravelNoteVO updateNote(Long userId, Long noteId, TravelNoteCreateDTO dto);
    void deleteNote(Long userId, Long noteId);
    List<TravelNoteVO> getHotNotes();
}
