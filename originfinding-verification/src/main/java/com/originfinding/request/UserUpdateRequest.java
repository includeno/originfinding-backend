package com.originfinding.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    Integer id;
    String username;
    String password;

    Integer roleId;
    Integer deleted;
}
