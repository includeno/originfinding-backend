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
