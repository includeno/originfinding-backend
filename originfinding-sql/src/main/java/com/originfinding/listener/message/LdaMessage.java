package com.originfinding.listener.message;

import com.originfinding.entity.SimRecord;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LdaMessage implements Serializable {
    Integer id;//数据库id
    String content;//文章内容

    String url;//文章地址
    String title;//文章标题
    String tag;//文章显示的标签
    Date time;//文章时间
    Integer valid;//记录 有效1 无效0

    String tfidftag;//根据LDA计算的标签
    String ldatag;//根据LDA计算的标签
    String simhash;
    Date updateTime;

    public static LdaMessage fromSimRecord(SimRecord simRecord,String content) {
        LdaMessage ldaMessage=new LdaMessage();
        ldaMessage.setId(simRecord.getId());
        ldaMessage.setContent(content);

        ldaMessage.setUrl(simRecord.getUrl());
        ldaMessage.setTitle(simRecord.getTitle());
        ldaMessage.setTag(simRecord.getTag());
        ldaMessage.setTime(simRecord.getTime());
        ldaMessage.setValid(simRecord.getValid());

        ldaMessage.setTfidftag(simRecord.getTfidftag());
        ldaMessage.setLdatag(simRecord.getLdatag());
        ldaMessage.setSimhash(simRecord.getSimhash());
        ldaMessage.setUpdateTime(simRecord.getUpdateTime());
        return ldaMessage;
    }
}
