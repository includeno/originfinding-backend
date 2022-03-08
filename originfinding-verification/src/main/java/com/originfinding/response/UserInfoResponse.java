package com.originfinding.response;

import com.originfinding.vo.UserRoleVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserInfoResponse {

    String username;
    String email;

    Date createTime;
    Date updateTime;
    Integer deleted;

    List<UserRoleVo> roles;

}
