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

    public final static List<Class> impls = getSubClassesFromInterface(ContentService.class);

    public static List<Class> getSubClassesFromInterface(Class<?> target) {
        return getSubclassesFromJar(target);
    }

    public static List<Class> getSubclassesFromJar(Class c) {

        Reflections reflections = new Reflections(
                ClasspathHelper.forPackage("com.originfinding.service"), Scanners.values());
        Set<Class> implementingTypes =
                reflections.getSubTypesOf(c);
        return implementingTypes.stream().collect(Collectors.toList());
    }
}
