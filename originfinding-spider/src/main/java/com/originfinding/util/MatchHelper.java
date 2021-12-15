package com.originfinding.util;

import com.google.common.collect.Lists;
import com.originfinding.service.ContentService;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;


//辅助匹配url的工具
@Slf4j
public class MatchHelper {

     public final static List<ClassPair> methodList=getMethodList(ContentService.class);

     public static List<ClassPair> getMethodList(Class<?> target) {
         ArrayList<Class<?>> subList=getInterfaceImpls(target);
         List<ClassPair> list=new ArrayList<>();
         for(Class c:subList){
             ClassPair pair=new ClassPair();
             Method m= null;
             try {
                 m = c.getMethod("match",new Class[]{String.class});
                 pair.setM(m);
                 pair.setC(c);
             } catch (NoSuchMethodException e) {
                 e.printStackTrace();
             }
             list.add(pair);
         }
         return list;

     }

    /**
     * 获取一个接口的所有实现类
     *
     * @param target
     * @return
     */
    public static ArrayList<Class<?>> getInterfaceImpls(Class<?> target) {
        ArrayList<Class<?>> subclassaes = Lists.newArrayList();
        try {
            // 判断class对象是否是一个接口
            if (target.isInterface()) {
                log.warn("getName:"+target.getClassLoader().getName());
                log.warn("empty path:"+target.getClassLoader().getResource("").getPath());
                log.warn("basePackage path:"+target.getClassLoader().getResource("./" + "com.originfinding.service".replaceAll("\\.", "/")).getPath());
                String basePackage = target.getClassLoader().getResource("./" + "com.originfinding.service".replaceAll("\\.", "/")).getPath();
                File[] files = new File(basePackage).listFiles();
                // 存放class路径的list
                ArrayList<String> classpaths = Lists.newArrayList();
                for (File file : files) {
                    // 扫描项目编译后的所有类
                    if (file.isDirectory()) {
                        listPackages(file.getName(), classpaths);
                    }
                }
                // 获取所有类,然后判断是否是 target 接口的实现类
                for (String classpath : classpaths) {
                    Class<?> classObject = Class.forName(classpath);
                    if (classObject.getSuperclass() == null) continue; // 判断该对象的父类是否为null
                    Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(classObject.getInterfaces()));
                    if (interfaces.contains(target)) {
                        subclassaes.add(Class.forName(classObject.getName()));
                    }
                }
            } else {
                throw new Exception("Class对象不是一个interface");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return subclassaes;
    }

    /**
     * 获取项目编译后的所有的.class的字节码文件
     * 这么做的目的是为了让 Class.forName() 可以加载类
     *
     * @param basePackage 默认包名
     * @param classes     存放字节码文件路径的集合
     * @return
     */
    public static void listPackages(String basePackage, List<String> classes) {
        URL url = ContentService.class.getClassLoader()
                .getResource("./" + basePackage.replaceAll("\\.", "/"));
        File directory = new File(url.getFile());
        for (File file : directory.listFiles()) {
            // 如果是一个目录就继续往下读取(递归调用)
            if (file.isDirectory()) {
                listPackages(basePackage + "." + file.getName(), classes);
            } else {
                // 如果不是一个目录,判断是不是以.class结尾的文件,如果不是则不作处理
                String classpath = file.getName();
                if (".class".equals(classpath.substring(classpath.length() - ".class".length()))) {
                    classes.add(basePackage + "." + classpath.replaceAll(".class", ""));
                }
            }
        }
    }
}
