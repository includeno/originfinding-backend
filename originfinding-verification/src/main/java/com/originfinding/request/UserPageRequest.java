package com.originfinding.request;

import lombok.Data;

@Data
public class UserPageRequest extends PageRequest {

    String username;
    String email;
    Integer deleted;
}
