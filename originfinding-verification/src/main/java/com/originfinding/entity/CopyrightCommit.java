package com.originfinding.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.originfinding.enums.CopyrightCommitCode;
import com.originfinding.enums.CopyrightCommitStatus;
import com.originfinding.request.CopyrightCommitRequest;
import lombok.Data;

import java.util.Date;

@Data
public class CopyrightCommit {

    @TableId(type = IdType.AUTO)
    Integer id;

    Integer userId;
    String url;
    String platform;
    String platformHash;
    String comment;
    Integer status;//CopyrightStatus内定义
    Integer auditId;//审核员ID

    @TableField(fill = FieldFill.INSERT)
    Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    @Version
    Integer version;
    @TableLogic
    Integer deleted;

    public static CopyrightCommit newInstance(CopyrightCommitRequest request){
        CopyrightCommit copyrightCommit=new CopyrightCommit();
        //数据
        copyrightCommit.setUserId(request.getUserId());
        copyrightCommit.setUrl(request.getUrl());
        copyrightCommit.setPlatform(request.getPlatform());
        copyrightCommit.setPlatformHash(request.getPlatformHash());
        copyrightCommit.setComment(request.getComment());
        copyrightCommit.setStatus(CopyrightCommitStatus.NOT_AUDIT.getCode());
        Date date=new Date();
        copyrightCommit.setCreateTime(date);
        copyrightCommit.setUpdateTime(date);
        copyrightCommit.setVersion(1);

        return copyrightCommit;
    }

    public static CopyrightCommit copyFromCopyrightCommitRequest(CopyrightCommitRequest request){
        CopyrightCommit copyrightCommit=new CopyrightCommit();
        if(request.getId()!=null){
            copyrightCommit.setId(request.getId());
        }
        copyrightCommit.setComment(request.getComment());
        return copyrightCommit;
    }
}
