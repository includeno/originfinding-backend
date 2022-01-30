package com.originfinding.service.sql;

import com.baomidou.mybatisplus.extension.service.IService;
import com.originfinding.entity.SpiderRecord;

public interface SpiderRecordService extends IService<SpiderRecord> {
    public int getLastId();
}
