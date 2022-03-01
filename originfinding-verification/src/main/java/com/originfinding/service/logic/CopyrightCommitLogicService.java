package com.originfinding.service.logic;

import com.originfinding.request.AuditRequest;
import com.originfinding.util.R;

public interface CopyrightCommitLogicService {

    //审核
    public R audit(AuditRequest request);
}
