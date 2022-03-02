package com.originfinding.request;

import lombok.Data;

@Data
public class AuditRequest {
    Integer id;//CopyrightCommit id
    Integer status;//变为的状态
    Integer auditId;//审核人
}
