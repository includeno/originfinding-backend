package com.originfinding.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    String username;
    String email;
    String password;

    Integer roleId;
}
