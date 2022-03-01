package com.originfinding.request;

import lombok.Data;

@Data
public class CopyrightCommitPageRequest extends PageRequest{

    String url;
    String email;
    String userId;
    Integer status;
    Integer deleted;
}
