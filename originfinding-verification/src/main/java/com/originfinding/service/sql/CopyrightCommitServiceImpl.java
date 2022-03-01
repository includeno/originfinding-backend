package com.originfinding.service.sql;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.originfinding.entity.CopyrightCommit;
import com.originfinding.mapper.CopyrightCommitMapper;
import org.springframework.stereotype.Service;

@Service
public class CopyrightCommitServiceImpl extends ServiceImpl<CopyrightCommitMapper, CopyrightCommit> implements CopyrightCommitService {
}
