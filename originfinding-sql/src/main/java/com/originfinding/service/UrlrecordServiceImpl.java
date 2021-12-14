package com.originfinding.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.originfinding.entity.SimRecord;
import com.originfinding.mapper.UrlrecordMapper;
import org.springframework.stereotype.Service;

@Service
public class UrlrecordServiceImpl extends ServiceImpl<UrlrecordMapper, SimRecord> implements UrlrecordService {
}
