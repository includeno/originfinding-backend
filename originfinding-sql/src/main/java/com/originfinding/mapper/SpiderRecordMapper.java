package com.originfinding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.originfinding.entity.SpiderRecord;
import org.springframework.stereotype.Component;

@Component
public interface SpiderRecordMapper extends BaseMapper<SpiderRecord> {
    public int getLastId(String url);
}
