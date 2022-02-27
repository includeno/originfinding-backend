package com.originfinding.request;

import lombok.Data;

@Data
public class UserLoginRequest {

    String email;
    String password;
}
