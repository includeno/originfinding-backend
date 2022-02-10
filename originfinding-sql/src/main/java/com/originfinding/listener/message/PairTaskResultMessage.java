package com.originfinding.listener.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class PairTaskResultMessage implements Serializable {
    Integer id;

    //特征指标
    String simlevelfirst ="";
    String simlevelsecond ="";
    Integer simparentId=-1;//如果是原创的直接为-1 短文本为-2 最相似的父亲 时间在文章时间之前
    Integer earlyparentId=-1;//如果是原创的直接为-1 短文本为-2 相似度在一定范围内的最早的父亲


}
