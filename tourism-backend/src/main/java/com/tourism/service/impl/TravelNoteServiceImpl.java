package com.tourism.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.common.BusinessException;
import com.tourism.dto.TravelNoteCreateDTO;
import com.tourism.entity.TravelNote;
import com.tourism.entity.User;
import com.tourism.mapper.TravelNoteMapper;
import com.tourism.mapper.UserMapper;
import com.tourism.service.TravelNoteService;
import com.tourism.vo.TravelNoteVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TravelNoteServiceImpl implements TravelNoteService {

    private final TravelNoteMapper mapper;
    private final UserMapper userMapper;
    private static final Set<String> ALLOWED_SORT = Set.of("create_time", "view_count", "like_count");

    public TravelNoteServiceImpl(TravelNoteMapper mapper, UserMapper userMapper) {
        this.mapper = mapper; this.userMapper = userMapper;
    }

    @Override
    public IPage<TravelNoteVO> listNotes(Integer page, Integer size, String keyword, String destination, String sortBy) {
        LambdaQueryWrapper<TravelNote> w = new LambdaQueryWrapper<>();
        w.eq(TravelNote::getStatus, 1);
        if (StringUtils.hasText(keyword)) w.and(wr -> wr.like(TravelNote::getTitle, keyword).or().like(TravelNote::getContent, keyword));
        if (StringUtils.hasText(destination)) w.eq(TravelNote::getDestination, destination);
        if (sortBy != null && ALLOWED_SORT.contains(sortBy)) {
            if ("view_count".equals(sortBy)) w.orderByDesc(TravelNote::getViewCount);
            else if ("like_count".equals(sortBy)) w.orderByDesc(TravelNote::getLikeCount);
            else w.orderByDesc(TravelNote::getCreateTime);
        } else w.orderByDesc(TravelNote::getCreateTime);
        return mapper.selectPage(new Page<>(page, size), w).convert(this::toVO);
    }

    @Override
    public TravelNoteVO getNoteById(Long id) {
        TravelNote note = mapper.selectById(id);
        if (note == null || note.getStatus() != 1) return null;
        note.setViewCount(note.getViewCount() + 1);
        mapper.updateById(note);
        return toVO(note);
    }

    @Override
    public TravelNoteVO createNote(Long userId, TravelNoteCreateDTO dto) {
        TravelNote note = new TravelNote();
        BeanUtils.copyProperties(dto, note);
        note.setUserId(userId);
        note.setViewCount(0); note.setLikeCount(0); note.setStatus(1);
        mapper.insert(note);
        return toVO(note);
    }

    @Override
    public TravelNoteVO updateNote(Long userId, Long noteId, TravelNoteCreateDTO dto) {
        TravelNote note = mapper.selectById(noteId);
        if (note == null || !note.getUserId().equals(userId)) throw new BusinessException("无权编辑此游记");
        BeanUtils.copyProperties(dto, note, "userId", "viewCount", "likeCount", "status");
        mapper.updateById(note);
        return toVO(note);
    }

    @Override
    public void deleteNote(Long userId, Long noteId) {
        TravelNote note = mapper.selectById(noteId);
        if (note == null || !note.getUserId().equals(userId)) throw new BusinessException("无权删除此游记");
        note.setStatus(0); mapper.updateById(note);
    }

    @Override
    public List<TravelNoteVO> getHotNotes() {
        return mapper.selectList(
            new LambdaQueryWrapper<TravelNote>().eq(TravelNote::getStatus, 1).orderByDesc(TravelNote::getViewCount).last("LIMIT 5")
        ).stream().map(this::toVO).collect(Collectors.toList());
    }

    private TravelNoteVO toVO(TravelNote n) {
        TravelNoteVO vo = new TravelNoteVO(); BeanUtils.copyProperties(n, vo);
        User u = userMapper.selectById(n.getUserId());
        if (u != null) { vo.setUsername(u.getNickname() != null ? u.getNickname() : u.getUsername()); vo.setUserAvatar(u.getAvatar()); }
        return vo;
    }
}
