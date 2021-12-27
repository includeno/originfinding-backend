package com.originfinding.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.originfinding.entity.SimRecord;
import com.originfinding.entity.SpiderRecord;
import com.originfinding.mapper.SimRecordMapper;
import com.originfinding.mapper.SpiderRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class SpiderRecordServiceImpl extends ServiceImpl<SpiderRecordMapper, SpiderRecord> implements SpiderRecordService {
}
