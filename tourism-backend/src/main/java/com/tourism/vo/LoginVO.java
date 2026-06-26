package com.tourism.vo;

public class LoginVO {

    private String token;
    private UserVO user;

    private LoginVO() {}

    public static LoginVO of(String token, UserVO user) {
        LoginVO vo = new LoginVO();
        vo.token = token;
        vo.user = user;
        return vo;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UserVO getUser() { return user; }
    public void setUser(UserVO user) { this.user = user; }
}
