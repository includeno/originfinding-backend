package com.originfinding.listener.message;

import com.originfinding.entity.SimRecord;
import lombok.Data;

import java.util.Date;

@Data
public class SparkTfidfTaskMessage {

    Integer id;//数据库id

    String url;//文章地址
    String title;//文章标题
    String tag;//文章显示的标签
    Date time;//文章时间
    String tfidftag;//根据LDA计算的标签

    String simhash;

    public static SparkTfidfTaskMessage fromSimRecord(SimRecord simRecord){
        SparkTfidfTaskMessage sparkTfidfTaskMessage =new SparkTfidfTaskMessage();
        sparkTfidfTaskMessage.setId(simRecord.getId());
        sparkTfidfTaskMessage.setUrl(simRecord.getUrl());
        sparkTfidfTaskMessage.setTitle(simRecord.getTitle());
        sparkTfidfTaskMessage.setTime(simRecord.getTime());
        sparkTfidfTaskMessage.setSimhash(simRecord.getSimhash());
        sparkTfidfTaskMessage.setTag(simRecord.getTag());
        sparkTfidfTaskMessage.setTfidftag(simRecord.getTfidftag());

        return sparkTfidfTaskMessage;
    }
}
