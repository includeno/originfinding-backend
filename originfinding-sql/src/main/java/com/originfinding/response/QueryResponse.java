package com.originfinding.response;

import java.util.Date;

public class QueryResponse {

    String url;//当前url
    String sim3;
    String sim4;
    Date updateTime;//上一次更新时间
    String simparentUrl;//根据parentId转换为对应的url
    String earlyparentUrl;//根据parentId转换为对应的url
    Integer manulsymbol =0;

    public QueryResponse(){
        this.url="";
        this.sim3="";
        this.sim4="";
        this.updateTime=new Date();
        this.simparentUrl="";
        this.earlyparentUrl="";
        this.manulsymbol=0;
    }
}
