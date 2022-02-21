package com.originfinding.algorithm.ansj;

import org.ansj.recognition.impl.StopRecognition;

import java.util.ArrayList;
import java.util.List;

public class Stops {

    public static StopRecognition getStops(){
        //声明停用词
        List<String> stops=new ArrayList<>();
        stops.add(".");
        stops.add("。");
        stops.add(",");
        stops.add("!");
        stops.add("-");
        stops.add("_");
        stops.add("——");
        stops.add("//");
        for(int i=0;i<26;i++){
            stops.add(String.valueOf('a'+i));
        }
        for(int i=0;i<9;i++){
            stops.add(String.valueOf('0'+i));
        }

        StopRecognition filter = new StopRecognition();
        //过滤停用词
        filter.insertStopWords(stops);

        //过滤词性
        // https://www.cnblogs.com/51zone/p/8012143.html
        filter.insertStopNatures("xu");
        filter.insertStopNatures("uj");
        filter.insertStopNatures("ul");
        filter.insertStopNatures("ude1");
        filter.insertStopNatures("ude2");
        filter.insertStopNatures("ude3");
        filter.insertStopNatures("u");
        filter.insertStopNatures("ule");
        filter.insertStopNatures("uguo");
        filter.insertStopNatures("e");
        filter.insertStopNatures("y");
        filter.insertStopNatures("o");
        filter.insertStopNatures("m");
        filter.insertStopNatures("f");
        filter.insertStopNatures("null");
        filter.insertStopNatures("w");
        filter.insertStopNatures("vshi");
        filter.insertStopNatures("vyou");



        filter.insertStopNatures("t");//时间词
        filter.insertStopNatures("tg");//时间词性语素

        filter.insertStopNatures("s");//处所词

        filter.insertStopNatures("f");//方位词


//        filter.insertStopNatures("vd");//副动词
//        filter.insertStopNatures("vn");//名动词
        filter.insertStopNatures("vshi");//动词“是”
        filter.insertStopNatures("vyou");//动词“有”
        filter.insertStopNatures("vf");//趋向动词
        filter.insertStopNatures("vx");//形式动词
        //filter.insertStopNatures("vi");//不及物动词（内动词）
        filter.insertStopNatures("vl");//动词性惯用语
        filter.insertStopNatures("vg");//动词性语素

        filter.insertStopNatures("a");//形容词
        filter.insertStopNatures("ad");//副形词
        filter.insertStopNatures("an");//名形词
        filter.insertStopNatures("ag");//形容词性语素
        filter.insertStopNatures("al");//形容词性惯用语

        filter.insertStopNatures("b");//区别词
        filter.insertStopNatures("bl");//区别词性惯用语

        filter.insertStopNatures("z");//状态词

        filter.insertStopNatures("r");//代词
        filter.insertStopNatures("rr");//人称代词
        filter.insertStopNatures("rz");//指示代词
        filter.insertStopNatures("rzt");//时间指示代词
        filter.insertStopNatures("rzs");//处所指示代词
        filter.insertStopNatures("rzv");//谓词性指示代词
        filter.insertStopNatures("ry");//疑问代词
        filter.insertStopNatures("ryt");//时间疑问代词
        filter.insertStopNatures("rys");//处所疑问代词
        filter.insertStopNatures("ryv");//谓词性疑问代词
        filter.insertStopNatures("rg");//代词性语素

        filter.insertStopNatures("m");//数词
        filter.insertStopNatures("mq");//数量词

        filter.insertStopNatures("q");//量词
        filter.insertStopNatures("qv");//动量词
        filter.insertStopNatures("qt");//时量词

        filter.insertStopNatures("d");//副词

        filter.insertStopNatures("p");//介词
        filter.insertStopNatures("pba");//介词“把”
        filter.insertStopNatures("pbei");//介词“被”

        filter.insertStopNatures("c");//连词
        filter.insertStopNatures("cc");//并列连词

        filter.insertStopNatures("u");//助词
        filter.insertStopNatures("uzhe");//着
        filter.insertStopNatures("ule");//了
        filter.insertStopNatures("uguo");//过
        filter.insertStopNatures("ude1");//的
        filter.insertStopNatures("ude2");//地
        filter.insertStopNatures("ude3");//得
        filter.insertStopNatures("usuo");//所
        filter.insertStopNatures("udeng");//等
        filter.insertStopNatures("uyy");//一样
        filter.insertStopNatures("udh");//的话
        filter.insertStopNatures("uls");//来讲
        filter.insertStopNatures("uzhi");//之
        filter.insertStopNatures("ulian");//连

        filter.insertStopNatures("e");//叹词

        filter.insertStopNatures("y");//语气词(delete

        filter.insertStopNatures("o");//拟声词
        filter.insertStopNatures("h");//前缀
        filter.insertStopNatures("k");//后缀
        filter.insertStopNatures("x");//字符串
        filter.insertStopNatures("xx");//非语素字
        filter.insertStopNatures("xu");//网址URL

        filter.insertStopNatures("w");//标点符号
        filter.insertStopNatures("wkz");//左括号，全角：（
        filter.insertStopNatures("wky");//右括号，全角：）
        filter.insertStopNatures("wyz");//左引号，全角：“
        filter.insertStopNatures("wyy");//右引号，全角：”
        filter.insertStopNatures("wj");//句号，全角：。
        filter.insertStopNatures("ww");//问号，全角：？
        filter.insertStopNatures("wt");//叹号，全角：！
        filter.insertStopNatures("wd");//逗号，全角：，
        filter.insertStopNatures("wf");//分号，全角：；
        filter.insertStopNatures("wn");//顿号，全角：、
        filter.insertStopNatures("wm");//冒号，全角：：
        filter.insertStopNatures("ws");//省略号，全角：……
        filter.insertStopNatures("wp");//破折号，全角：——
        filter.insertStopNatures("wb");//百分号千分号，全角：％
        filter.insertStopNatures("wh");//单位符号，全角：￥
        return filter;
    }

    public static StopRecognition getStops2(){
        //声明停用词
        List<String> stops=new ArrayList<>();
        stops.add(".");
        stops.add("。");
        stops.add(",");
        stops.add("!");
        stops.add("//");
        stops.add("#");

        StopRecognition filter = new StopRecognition();
        //过滤停用词
        filter.insertStopWords(stops);

        //过滤词性 https://www.cnblogs.com/51zone/p/8012143.html
        filter.insertStopNatures("xu");
        filter.insertStopNatures("uj");
        filter.insertStopNatures("ul");
        filter.insertStopNatures("null");
        filter.insertStopNatures("w");

        return filter;
    }


}
