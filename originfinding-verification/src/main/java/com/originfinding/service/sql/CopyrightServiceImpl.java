package com.originfinding.service.sql;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.originfinding.entity.Copyright;
import com.originfinding.mapper.CopyrightMapper;
import org.springframework.stereotype.Service;

@Service
public class CopyrightServiceImpl extends ServiceImpl<CopyrightMapper, Copyright> implements CopyrightService{
}
