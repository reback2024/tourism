package com.tourism.service;

import com.tourism.vo.UserVO;

public interface UserService {

    /** 获取当前用户信息 */
    UserVO getCurrentUserInfo(Long userId);

    /** 更新用户信息 */
    void updateUser(Long userId, UserVO userVO);
}
