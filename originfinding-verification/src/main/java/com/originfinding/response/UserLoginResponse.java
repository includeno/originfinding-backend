package com.originfinding.response;

import com.originfinding.entity.Permission;
import com.originfinding.entity.Role;
import com.originfinding.entity.User;
import com.originfinding.vo.RolePermissionVo;
import com.originfinding.vo.UserRoleVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserLoginResponse {

    User user;
    List<UserRoleVo> roles;
    List<RolePermissionVo> permissions;
    String token;
    Date expireAt;
}
