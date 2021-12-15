package com.originfinding.util;

import com.google.common.collect.Lists;
import com.originfinding.service.ContentService;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;


//辅助匹配url的工具
@Slf4j
public class MatchHelper {

     public final static List<ClassPair> methodList=getMethodList(ContentService.class);

    public static List<ClassPair> getMethodList(Class<?> target) {
        List<Class> subList = getSubclassesFromJar(target);
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

     public static List<ClassPair> getMethodList2(Class<?> target) {
         List<Class> subList=getAllInterfaceAchieveClass(target);
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

                String basePackage = target.getClassLoader().getResource("").getPath();
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


    /**
     * 获取所有接口的实现类
     * @return
     */
    public static List<Class> getAllInterfaceAchieveClass(Class clazz){
        ArrayList<Class> list = new ArrayList<>();
        //判断是否是接口
        if (clazz.isInterface()) {
            try {
                ArrayList<Class> allClass = getAllClassByPath(clazz.getPackage().getName());
                /**
                 * 循环判断路径下的所有类是否实现了指定的接口
                 * 并且排除接口类自己
                 */
                for (int i = 0; i < allClass.size(); i++) {

                    //排除抽象类
                    if(Modifier.isAbstract(allClass.get(i).getModifiers())){
                        continue;
                    }
                    //判断是不是同一个接口
                    if (clazz.isAssignableFrom(allClass.get(i))) {
                        if (!clazz.equals(allClass.get(i))) {
                            list.add(allClass.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("出现异常");
            }
        }
        return list;
    }

    /**
     * 从指定路径下获取所有类
     * @return
     */
    public static ArrayList<Class> getAllClassByPath(String packagename){
        ArrayList<Class> list = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packagename.replace('.', '/');
        try {
            ArrayList<File> fileList = new ArrayList<>();
            Enumeration<URL> enumeration = classLoader.getResources(path);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                fileList.add(new File(url.getFile()));
            }
            for (int i = 0; i < fileList.size(); i++) {
                list.addAll(findClass(fileList.get(i),packagename));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 如果file是文件夹，则递归调用findClass方法，或者文件夹下的类
     * 如果file本身是类文件，则加入list中进行保存，并返回
     * @param file
     * @param packagename
     * @return
     */
    private static ArrayList<Class> findClass(File file,String packagename) {
        ArrayList<Class> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }
        File[] files = file.listFiles();
        for (File file2 : files) {
            if (file2.isDirectory()) {
                assert !file2.getName().contains(".");//添加断言用于判断
                ArrayList<Class> arrayList = findClass(file2, packagename+"."+file2.getName());
                list.addAll(arrayList);
            }else if(file2.getName().endsWith(".class")){
                try {
                    //保存的类文件不需要后缀.class
                    list.add(Class.forName(packagename + '.' + file2.getName().substring(0,
                            file2.getName().length()-6)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


    public static List<Class> getSubclassesFromJar(Class c){

        Reflections reflections = new Reflections(
                ClasspathHelper.forPackage("com.originfinding.service"), Scanners.values());
        Set<Class<? extends c>> implementingTypes =
                reflections.getSubTypesOf(c);
        return implementingTypes.stream().collect(Collectors.toList());
    }
}
