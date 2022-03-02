package com.originfinding.request;

import lombok.Data;

@Data
public class CopyrightCommitPageRequest extends PageRequest{

    String url;
    String email;//与userId互斥，按照userId、email的顺序查询，仅查询第一个非空条件
    String userId;
    Integer status;
    Integer auditId;

    Integer deleted;
}
