package com.originfinding.listener.message;

import com.originfinding.entity.SimRecord;
import lombok.Data;

import java.util.Date;

@Data
public class SparkTaskMessage {

    Integer id;//数据库id

    String url;//文章地址
    String title;//文章标题
    String content;//文章内容
    String tag;//文章显示的标签
    Date time;//文章时间

    String simhash;
    String keywords;//通过分词方法得到

    public static SparkTaskMessage fromSimRecord(SimRecord simRecord,String content){
        SparkTaskMessage sparkTaskMessage=new SparkTaskMessage();
        sparkTaskMessage.setId(simRecord.getId());
        sparkTaskMessage.setUrl(simRecord.getUrl());
        sparkTaskMessage.setTitle(simRecord.getTitle());
        sparkTaskMessage.setContent(content);
        sparkTaskMessage.setSimhash(simRecord.getSimhash());
        sparkTaskMessage.setTag(simRecord.getTag());
        return sparkTaskMessage;
    }
}
