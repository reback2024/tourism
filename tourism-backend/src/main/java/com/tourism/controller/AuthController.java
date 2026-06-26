package com.tourism.controller;

import com.tourism.common.Result;
import com.tourism.dto.LoginDTO;
import com.tourism.dto.RegisterDTO;
import com.tourism.service.AuthService;
import com.tourism.vo.LoginVO;
import com.tourism.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        LoginVO vo = authService.login(dto);
        return Result.ok("登录成功", vo);
    }

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterDTO dto) {
        UserVO vo = authService.register(dto);
        return Result.ok("注册成功", vo);
    }
}
