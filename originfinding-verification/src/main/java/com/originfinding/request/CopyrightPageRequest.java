package com.originfinding.request;

import lombok.Data;

@Data
public class CopyrightPageRequest extends PageRequest{

    String url;
    String email;//与auditId互斥，按照auditId、email的顺序查询，仅查询第一个非空条件
    Integer auditId;
    Integer status;
}
