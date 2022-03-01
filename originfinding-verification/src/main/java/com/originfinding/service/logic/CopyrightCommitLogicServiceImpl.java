package com.originfinding.service.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.originfinding.config.RedisKey;
import com.originfinding.entity.Copyright;
import com.originfinding.entity.CopyrightCommit;
import com.originfinding.enums.AuditCode;
import com.originfinding.enums.CopyrightCommitStatus;
import com.originfinding.request.AuditRequest;
import com.originfinding.service.sql.CopyrightCommitService;
import com.originfinding.service.sql.CopyrightService;
import com.originfinding.service.sql.UserService;
import com.originfinding.util.R;
import com.originfinding.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CopyrightCommitLogicServiceImpl implements CopyrightCommitLogicService{
    @Autowired
    Gson gson;

    @Autowired
    CopyrightCommitService copyrightCommitService;

    @Autowired
    CopyrightService copyrightService;

    @Autowired
    UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    @Override
    public R audit(AuditRequest request) {
        //检查权限是否满足
        List<UserRoleVo> roles=userService.selectRoles(request.getUserId());
        Long count=roles.stream().filter(a->a.getCode().equals("admin")||a.getCode().equals("audit")).count();
        if(count==0){
            return R.build(AuditCode.AUTH_ERROR,null);
        }
        //检查记录是否存在
        CopyrightCommit entity=copyrightCommitService.getById(request.getId());
        if(entity==null||entity.getUrl()==null){
            return R.build(AuditCode.RECORD_ERROR,null);
        }

        QueryWrapper<Copyright> copyrightQueryWrapper=new QueryWrapper<>();
        copyrightQueryWrapper.eq("url",entity.getUrl());
        Copyright copyright=copyrightService.getOne(copyrightQueryWrapper);
        String key=RedisKey.copyrightKey(entity.getUrl());
        //审核员通过请求
        if(request.getStatus().equals(CopyrightCommitStatus.PASSED)){
            //保存至数据库
            if(copyright==null){
                copyright=new Copyright();
            }
            Date time=new Date();
            copyright.setCreateTime(time);
            copyright.setUpdateTime(time);
            boolean sqlresult=copyrightService.saveOrUpdate(copyright);
            if(sqlresult==true){
                //保存缓存
                stringRedisTemplate.opsForValue().set(key,gson.toJson(copyright));
                return R.build(AuditCode.OK,null);
            }
            else{
                return R.build(AuditCode.DB_ERROR,null);
            }
        }
        //审核员不通过请求
        else if(request.getStatus().equals(CopyrightCommitStatus.NOT_PASSED)){
            if(copyright!=null&&copyright.getId()!=null){
                Date time=new Date();
                copyright.setRequestId(-1);//删除该请求的关联关系
                copyright.setCreateTime(time);
                copyright.setUpdateTime(time);
                boolean sqlresult=copyrightService.saveOrUpdate(copyright);

                if(sqlresult){
                    //清空缓存
                    stringRedisTemplate.opsForValue().set(key,null);
                }
                else {
                    return R.build(AuditCode.DB_ERROR,null);
                }
            }
        }
        return R.build(AuditCode.OK,null);
    }
}
