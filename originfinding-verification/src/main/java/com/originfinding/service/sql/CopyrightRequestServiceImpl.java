package com.originfinding.service.sql;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.originfinding.entity.CopyrightRequest;
import com.originfinding.mapper.CopyrightRequestMapper;
import org.springframework.stereotype.Service;

@Service
public class CopyrightRequestServiceImpl extends ServiceImpl<CopyrightRequestMapper, CopyrightRequest> implements CopyrightRequestService{
}
