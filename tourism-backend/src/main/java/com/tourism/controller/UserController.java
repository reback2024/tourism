package com.tourism.controller;

import com.tourism.common.Result;
import com.tourism.security.SecurityUtils;
import com.tourism.service.UserService;
import com.tourism.vo.UserVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        UserVO vo = userService.getCurrentUserInfo(userId);
        return Result.ok(vo);
    }

    @PutMapping("/update")
    public Result<Void> updateUser(@RequestBody UserVO userVO) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.updateUser(userId, userVO);
        return Result.ok("更新成功", null);
    }
}
