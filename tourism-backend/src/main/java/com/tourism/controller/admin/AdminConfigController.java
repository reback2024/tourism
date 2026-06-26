package com.tourism.controller.admin;

import com.tourism.common.Result;
import com.tourism.entity.SysConfig;
import com.tourism.mapper.SysConfigMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/configs")
public class AdminConfigController {

    private final SysConfigMapper mapper;
    public AdminConfigController(SysConfigMapper m) { this.mapper = m; }

    @GetMapping
    public Result<List<SysConfig>> list() { return Result.ok(mapper.selectList(null)); }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysConfig config) {
        SysConfig exist = mapper.selectById(id);
        if (exist != null) { exist.setConfigValue(config.getConfigValue()); mapper.updateById(exist); }
        return Result.ok("配置更新成功", null);
    }
}
