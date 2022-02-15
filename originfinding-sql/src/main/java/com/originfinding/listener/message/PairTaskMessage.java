package com.originfinding.listener.message;

import com.originfinding.entity.SimRecord;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PairTaskMessage implements Serializable {
    Integer id;

    Integer view=-1;//文章浏览量 -1代表无法获取数据
    Date time;//文章时间

    String simhash="";
    Integer valid=1;//记录 有效1 无效0

    //特征指标
    Integer simlevelfirst =-1;
    Integer simlevelsecond =-1;
    Integer simparentId=-1;//如果是原创的直接为-1 短文本为-2 最相似的父亲 时间在文章时间之前
    Integer earlyparentId=-1;//如果是原创的直接为-1 短文本为-2 相似度在一定范围内的最早的父亲

    //人工审核标记
    Integer manulsymbol =0;//人工审核标记关联的原创文章id 0表示未设置 1表示人工标识为原创
    Integer version=1;

    public static PairTaskMessage fromSimRecord(SimRecord temp) {
        PairTaskMessage pairTaskMessage=new PairTaskMessage();
        pairTaskMessage.setId(temp.getId());

        pairTaskMessage.setView(temp.getView());
        pairTaskMessage.setTime(temp.getTime());

        pairTaskMessage.setSimhash(temp.getSimhash());
        pairTaskMessage.setValid(temp.getValid());

        pairTaskMessage.setSimlevelfirst(temp.getSimlevelfirst());
        pairTaskMessage.setSimlevelsecond(temp.getSimlevelsecond());
        pairTaskMessage.setSimparentId(temp.getSimparentId());
        pairTaskMessage.setEarlyparentId(temp.getEarlyparentId());

        pairTaskMessage.setManulsymbol(temp.getManulsymbol());
        pairTaskMessage.setVersion(temp.getVersion());
        return pairTaskMessage;
    }
}
