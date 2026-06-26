package com.tourism.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tourism.common.PageResult;
import com.tourism.common.Result;
import com.tourism.entity.User;
import com.tourism.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserMapper mapper;
    public AdminUserController(UserMapper m) { this.mapper = m; }

    @GetMapping
    public Result<PageResult<User>> list(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<User> w = new LambdaQueryWrapper<>();
        if (keyword != null) w.like(User::getUsername, keyword).or().like(User::getNickname, keyword);
        w.orderByDesc(User::getCreateTime);
        IPage<User> p = mapper.selectPage(new Page<>(page, size), w);
        p.getRecords().forEach(u -> u.setPassword(null)); // 不返回密码
        return Result.ok(PageResult.of(p));
    }

    @PutMapping("/{id}/status")
    public Result<Void> toggleStatus(@PathVariable Long id) {
        User u = mapper.selectById(id); if (u != null) { u.setStatus(u.getStatus() == 1 ? 0 : 1); mapper.updateById(u); }
        return Result.ok("操作成功", null);
    }

    @PutMapping("/{id}/role")
    public Result<Void> changeRole(@PathVariable Long id, @RequestBody User body) {
        User u = mapper.selectById(id); if (u != null) { u.setRole(body.getRole()); mapper.updateById(u); }
        return Result.ok("角色变更成功", null);
    }
}
