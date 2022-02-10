package com.originfinding.listener.message;

import com.originfinding.entity.SimRecord;
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
    Integer view=-1;//文章浏览量 -1代表无法获取数据
    Integer valid;//记录 有效1 无效0

    String simhash;
    Date updateTime;

    public static PairTaskMessage fromSimRecord(SimRecord temp) {
        PairTaskMessage pairTaskMessage=new PairTaskMessage();
        pairTaskMessage.setId(temp.getId());

        pairTaskMessage.setUrl(temp.getUrl());
        pairTaskMessage.setTitle(temp.getTitle());
        pairTaskMessage.setTag(temp.getTag());
        pairTaskMessage.setTime(temp.getTime());
        pairTaskMessage.setView(temp.getView());
        pairTaskMessage.setId(temp.getId());

        pairTaskMessage.setSimhash(temp.getSimhash());
        pairTaskMessage.setUpdateTime(temp.getUpdateTime());

        return pairTaskMessage;
    }
}
