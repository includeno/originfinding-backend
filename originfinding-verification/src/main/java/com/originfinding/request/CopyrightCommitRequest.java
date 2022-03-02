package com.originfinding.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class CopyrightCommitRequest {
    Integer id;
    String email;//验证用户身份 额外字段
    Integer userId;
    String url;
    String platform;
    String platformHash;

    String comment;
    Integer status;//CopyrightStatus内定义
    Integer auditId;

    Integer deleted;
}
