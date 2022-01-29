package com.originfinding.listener.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PairTaskMessage implements Serializable {
    Integer id;//数据库id

    String url;//文章地址
    String title;//文章标题
    String tag;//文章显示的标签
    Date time;//文章时间
    Integer valid;//记录 有效1 无效0

    String tfidftag;//根据LDA计算的标签
    String ldatag;//根据LDA计算的标签
    String simhash;
    Date updateTime;

    public static PairTaskMessage fromLdaMessage(LdaMessage message){
        PairTaskMessage pairTaskMessage =new PairTaskMessage();
        pairTaskMessage.setId(message.getId());

        pairTaskMessage.setUrl(message.getUrl());
        pairTaskMessage.setTag(message.getTag());
        pairTaskMessage.setTitle(message.getTitle());
        pairTaskMessage.setTime(message.getTime());
        pairTaskMessage.setValid(message.getValid());

        pairTaskMessage.setSimhash(message.getSimhash());
        pairTaskMessage.setLdatag(message.getLdatag());
        pairTaskMessage.setTfidftag(message.getTfidftag());
        pairTaskMessage.setUpdateTime(message.getUpdateTime());
        return pairTaskMessage;
    }
}
