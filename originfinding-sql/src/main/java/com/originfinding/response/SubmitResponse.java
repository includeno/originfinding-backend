package com.originfinding.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SubmitResponse {

    List<SubmitResponseEntity> list;

    @Data
    public static class SubmitResponseEntity{
        String url;//当前url
        String sim3;
        String sim4;
        String sim5;
        Date updateTime;//上一次更新时间
        String parentUrl;//根据parentId转换为对应的url

        public SubmitResponseEntity(){
            this.url="";
            this.sim3="";
            this.sim4="";
            this.sim5="";
            this.updateTime=new Date();
            this.parentUrl="";
        }
    }
}
