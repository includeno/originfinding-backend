package com.originfinding.listener.message;

import com.originfinding.entity.SimRecord;
import lombok.Data;

import java.util.Date;

@Data
public class SparkLdaTaskMessage {

    Integer id;//数据库id
    String url;//文章地址
    String title;//文章标题
    String tag;//文章显示的标签
    Date time;//文章时间
    String ldatag;//根据LDA计算的标签

    String simhash;

    public static SparkLdaTaskMessage fromSimRecord(SimRecord simRecord, String content){
        SparkLdaTaskMessage sparkLdaTaskMessage =new SparkLdaTaskMessage();
        sparkLdaTaskMessage.setId(simRecord.getId());
        sparkLdaTaskMessage.setUrl(simRecord.getUrl());
        sparkLdaTaskMessage.setTitle(simRecord.getTitle());
        sparkLdaTaskMessage.setTime(simRecord.getTime());
        sparkLdaTaskMessage.setSimhash(simRecord.getSimhash());
        sparkLdaTaskMessage.setTag(simRecord.getTag());

        return sparkLdaTaskMessage;
    }
}
