package com.tourism.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.dto.TravelNoteCreateDTO;
import com.tourism.security.SecurityUtils;
import com.tourism.service.TravelNoteService;
import com.tourism.vo.TravelNoteVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel-notes")
public class TravelNoteController {

    private final TravelNoteService service;
    public TravelNoteController(TravelNoteService service) { this.service = service; }

    @GetMapping
    public Result<PageResult<TravelNoteVO>> list(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestParam(required = false) String keyword,
                                                   @RequestParam(required = false) String destination,
                                                   @RequestParam(required = false) String sortBy) {
        IPage<TravelNoteVO> result = service.listNotes(page, size, keyword, destination, sortBy);
        return Result.ok(PageResult.of(result));
    }

    @GetMapping("/{id}")
    public Result<TravelNoteVO> detail(@PathVariable Long id) {
        TravelNoteVO vo = service.getNoteById(id);
        if (vo == null) return Result.fail(404, "游记不存在");
        return Result.ok(vo);
    }

    @PostMapping
    public Result<TravelNoteVO> create(@Valid @RequestBody TravelNoteCreateDTO dto) {
        return Result.ok("发布成功", service.createNote(SecurityUtils.getCurrentUserId(), dto));
    }

    @PutMapping("/{id}")
    public Result<TravelNoteVO> update(@PathVariable Long id, @Valid @RequestBody TravelNoteCreateDTO dto) {
        return Result.ok("更新成功", service.updateNote(SecurityUtils.getCurrentUserId(), id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.deleteNote(SecurityUtils.getCurrentUserId(), id);
        return Result.ok("删除成功", null);
    }

    @GetMapping("/hot")
    public Result<List<TravelNoteVO>> hot() {
        return Result.ok(service.getHotNotes());
    }
}
