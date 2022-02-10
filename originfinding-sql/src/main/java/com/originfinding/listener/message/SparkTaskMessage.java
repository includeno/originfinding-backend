package com.originfinding.listener.message;

import com.originfinding.entity.SimRecord;
import lombok.Data;

import java.util.Date;

@Data
public class SparkTaskMessage {

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

    public static SparkTaskMessage fromSimRecord(SimRecord simRecord){
        SparkTaskMessage sparkTfidfTaskMessage =new SparkTaskMessage();
        sparkTfidfTaskMessage.setId(simRecord.getId());
        sparkTfidfTaskMessage.setUrl(simRecord.getUrl());
        sparkTfidfTaskMessage.setTitle(simRecord.getTitle());
        sparkTfidfTaskMessage.setTag(simRecord.getTag());
        sparkTfidfTaskMessage.setTime(simRecord.getTime());
        sparkTfidfTaskMessage.setValid(simRecord.getValid());

        sparkTfidfTaskMessage.setSimhash(simRecord.getSimhash());
        sparkTfidfTaskMessage.setUpdateTime(simRecord.getUpdateTime());

        return sparkTfidfTaskMessage;
    }
}
