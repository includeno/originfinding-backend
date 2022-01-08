package com.originfinding.algorithm.ansj;

import com.google.gson.Gson;
import com.originfinding.algorithm.WordPair;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.math.BigInteger;
import java.util.*;

@Data
@Slf4j
public class AnsjSimHash {
    String token;//源文本
    BigInteger strSimHash;//Simhash
    Integer hashbits = 64;
    List<String> words;//Top 关键词列表
    List<WordPair> wordsPair;//Top 关键词列表 带次数
    Integer top=30;//Top 关键词数量
    Integer hashcode;//源文本hashcode 唯一标识

    public AnsjSimHash(String token){
        StopRecognition filter = Stops.getStops();
        init(token,64,filter,30);
    }

    public AnsjSimHash(String token, Integer hashbits){
        StopRecognition filter = Stops.getStops();
        init(token,hashbits,filter,30);
    }

    public AnsjSimHash(String token, Integer hashbits, StopRecognition filter){
        init(token,hashbits,filter,30);
    }

    public AnsjSimHash(String token, Integer hashbits, StopRecognition filter,Integer top){
        init(token,hashbits,filter,top);
    }


    private void init(String token, Integer hashbits, StopRecognition filter,Integer top){
        assert top>=1;

        this.token=token;
        this.hashbits=hashbits;
        this.hashcode=token.hashCode();
        this.top=top;
        this.strSimHash=simHash(this.token,this.hashbits,filter);
    }

    private BigInteger simHash(String token,Integer hashbits,StopRecognition filter) {
        token=token.toLowerCase(Locale.ROOT);//统一英文小写
        int[] v = new int[hashbits];

        List<Term> termList = ToAnalysis.parse(token.toString().replaceAll(",", ""))
                .recognition(filter).getTerms(); // 对字符串进行分词

        //对分词的一些特殊处理 : 比如: 根据词性添加权重 , 过滤掉标点符号 , 过滤超频词汇等;
        Map<String, Integer> weightOfNature = new TreeMap<>(); // 词性的权重
        weightOfNature.put("n", 3); //给名词的权重是3;
        Map<String, String> stopNatures = new HashMap<String, String>();//停用的词性 如一些标点符号之类的;
        stopNatures.put("w", "");
        stopNatures.put("u", "");
        stopNatures.put("m", "");
        int overCount = 8; //设定超频词汇的界限 ;
        Map<String, Integer> wordCount = new HashMap<String, Integer>();

        for (Term term : termList) {
            String word = term.getName(); //分词字符串
            String nature = term.getNatureStr(); // 分词属性;
            //log.info("word: "+word+" nature:"+nature);
            //  过滤超频词
            if (wordCount.containsKey(word)) {
                int count = wordCount.get(word);
                if (count > overCount) {
                    continue;
                }
                wordCount.put(word, count + 1);
            } else {
                wordCount.put(word, 1);
            }

            // 过滤停用词性
            if (stopNatures.containsKey(nature)) {
                continue;
            }

            // 2、将每一个分词hash为一组固定长度的数列.比如 64bit 的一个整数.
            BigInteger t = hash(word,hashbits);
            for (int i = 0; i < hashbits; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                // 3、建立一个长度为64的整数数组(假设要生成64位的数字指纹,也可以是其它数字),
                // 对每一个分词hash后的数列进行判断,如果是1000...1,那么数组的第一位和末尾一位加1,
                // 中间的62位减一,也就是说,逢1加1,逢0减1.一直到把所有的分词hash数列全部判断完毕.
                int weight = 1;  //添加权重
                if (weightOfNature.containsKey(nature)) {
                    weight = weightOfNature.get(nature);
                }
                if (t.and(bitmask).signum() != 0) {
                    // 这里是计算整个文档的所有特征的向量和
                    v[i] += weight;
                } else {
                    v[i] -= weight;
                }
            }
        }

        PriorityQueue<WordPair> queue=new PriorityQueue<>((a, b)->{return b.getCount()-a.getCount();});//
        for(Map.Entry<String,Integer> entry:wordCount.entrySet()){
            queue.offer(new WordPair(entry.getKey(),entry.getValue()));
        }
        words=new ArrayList<>();
        wordsPair=new ArrayList<>();
        if(queue.size()>=top){
            for(int i=0;i<top;i++){
                WordPair t=queue.poll();
                words.add(t.getWord());
                wordsPair.add(t);
            }
        }
        else{
            while (!queue.isEmpty()){
                WordPair t=queue.poll();
                words.add(t.getWord());
                wordsPair.add(t);
            }
        }
        log.info("token:"+this.hashcode+" =>"+new Gson().toJson(words));

        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < hashbits; i++) {
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }
        return fingerprint;
    }

    /**
     * 对单个的分词进行hash计算;
     * @param source
     * @return
     */
    private BigInteger hash(String source,Integer hashbits) {
        if (source == null || source.length() == 0) {
            return new BigInteger("0");
        } else {
            /**
             * 当sourece 的长度过短，会导致hash算法失效，因此需要对过短的词补偿
             */
            while (source.length() < 3) {
                source = source + source.charAt(0);
            }
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(hashbits).subtract(new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }

    /**
     * 计算海明距离,海明距离越小说明越相似;
     * @param from　源
     * @param to 目标
     * @return
     */
    public static int hammingDistance(AnsjSimHash from, AnsjSimHash to) {
        BigInteger m = new BigInteger("1").shiftLeft(from.getHashbits()).subtract(
                new BigInteger("1"));
        BigInteger x = from.getStrSimHash().xor(to.getStrSimHash()).and(m);
        int tot = 0;
        while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("1")));
        }
        return tot;
    }

    /**
     * 计算海明距离,海明距离越小说明越相似;
     * @param hashbits 位数
     * @param from　源
     * @param to 目标
     * @return
     */
    public static int hammingDistance(Integer hashbits,BigInteger from, BigInteger to) {
        assert from.bitLength()==hashbits;
        assert to.bitLength()==hashbits;
        BigInteger m = new BigInteger("1").shiftLeft(hashbits).subtract(
                new BigInteger("1"));
        BigInteger x = from.xor(to).and(m);
        int count = 0;
        while (x.signum() != 0) {
            count += 1;
            x = x.and(x.subtract(new BigInteger("1")));
        }
        return count;
    }

    /**
     * 计算相似度;
     * @param from 源
     * @param to 目标
     * @return
     */
    public static double getSemblance(AnsjSimHash from, AnsjSimHash to){
        double i = (double) AnsjSimHash.hammingDistance(from,to);
        return 1 - i/from.getHashbits() ;
    }

    /**
     * 计算相似度;
     * @param hashbits 位数
     * @param from　源
     * @param to 目标
     * @return
     */
    public static double getSemblance(Integer hashbits,BigInteger from, BigInteger to){
        double i = (double) AnsjSimHash.hammingDistance(hashbits,from,to);
        return 1 - i/hashbits ;
    }
}

