package com.tourism.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /** 获取当前登录用户信息 */
    public static JwtUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            return (JwtUserDetails) authentication.getPrincipal();
        }
        return null;
    }

    /** 获取当前登录用户ID */
    public static Long getCurrentUserId() {
        JwtUserDetails user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    /** 获取当前登录用户角色 */
    public static String getCurrentUserRole() {
        JwtUserDetails user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }

    /** 判断当前用户是否为管理员 */
    public static boolean isAdmin() {
        return "ADMIN".equals(getCurrentUserRole());
    }

    /** 判断是否已登录 */
    public static boolean isAuthenticated() {
        return getCurrentUser() != null;
    }
}
