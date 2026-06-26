package com.tourism.service;

import com.tourism.dto.LoginDTO;
import com.tourism.dto.RegisterDTO;
import com.tourism.vo.LoginVO;
import com.tourism.vo.UserVO;

public interface AuthService {

    /** 注册 */
    UserVO register(RegisterDTO dto);

    /** 登录 */
    LoginVO login(LoginDTO dto);
}
