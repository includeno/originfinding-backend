package com.originfinding.listener.message;

import com.originfinding.entity.SimRecord;
import lombok.Data;

import java.util.Date;

@Data
public class SparkLdaMessage {

    Integer id;//数据库id

    String url;//文章地址
    String title;//文章标题
    String content;//文章内容
    String tag;//文章显示的标签
    Date time;//文章时间

    String simhash;
    String ldatag;//根据LDA计算的标签

    public static SparkLdaMessage fromSimRecord(SimRecord simRecord, String content){
        SparkLdaMessage sparkLDAMessage=new SparkLdaMessage();
        sparkLDAMessage.setId(simRecord.getId());
        sparkLDAMessage.setUrl(simRecord.getUrl());
        sparkLDAMessage.setTitle(simRecord.getTitle());
        sparkLDAMessage.setTime(simRecord.getTime());
        sparkLDAMessage.setContent(content);
        sparkLDAMessage.setSimhash(simRecord.getSimhash());
        sparkLDAMessage.setTag(simRecord.getTag());

        return sparkLDAMessage;
    }
}
