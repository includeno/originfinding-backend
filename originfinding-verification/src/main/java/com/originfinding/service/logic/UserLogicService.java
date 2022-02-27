package com.originfinding.service.logic;

import com.originfinding.enums.LoginCode;
import com.originfinding.enums.RegisterCode;

public interface UserLogicService {
    //注册
    public RegisterCode register(String username, String password, String email, Integer roleId);

    //登录 仅通过email方式登录
    public LoginCode login(String email, String password);

    //验证
    public boolean isNameAvaible(String username);
    public boolean isEmailAvaible(String username);

}
