package com.originfinding.service.sql;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.originfinding.entity.SimRecord;
import com.originfinding.mapper.SimRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class SimRecordServiceImpl extends ServiceImpl<SimRecordMapper, SimRecord> implements SimRecordService {
}
