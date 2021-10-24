package com.github.raedev.dex;

import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 移除类
 *
 * @author RAE
 * @date 2021/10/2021/10/24
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
final class ExcludeClassHelper {

    public static boolean DEBUG = false;

    @Nullable
    private static DexExcludePlugin sDexExcludePlugin;

    public static void onConfig(DexExcludePlugin dexExcludePlugin) {
        sDexExcludePlugin = dexExcludePlugin;
    }

    @Nonnull
    public static Set<? extends ClassDef> excludeClasses(Set<? extends ClassDef> classes) {
        if (sDexExcludePlugin == null) {
            return classes;
        }
        Set<ClassDef> result = new HashSet<>();
        for (ClassDef cls : classes) {
            if (canInclude(cls.getType())) {
                result.add(cls);
            }
        }
        return result;
    }

    @Nonnull
    public static List<String> excludeInterfaces(List<String> interfaces) {
        if (sDexExcludePlugin == null) {
            return interfaces;
        }
        List<String> result = new ArrayList<>();
        for (String anInterface : interfaces) {
            if (canInclude(anInterface)) {
                result.add(anInterface);
            }
        }
        return result;
    }

    @Nonnull
    public static Set<? extends Annotation> excludeAnnotations(Set<? extends Annotation> annotations) {
        if (sDexExcludePlugin == null) {
            return annotations;
        }
        Set<Annotation> result = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (canInclude(annotation.getType())) {
                result.add(annotation);
            }
        }
        return result;
    }

    private static boolean canInclude(String type) {
        if (sDexExcludePlugin == null) {
            return true;
        }
        String className = typeToClassName(type);
        List<String> rules = sDexExcludePlugin.mDexExcludeExtension.excludes;

        for (String rule : rules) {
            if (rule.endsWith(".**") && matchingAllRule(rule, className)) {
                // 处理子包
                if (DEBUG) {
                    Log.d("排除类：" + className);
                }
                return false;
            } else if (rule.endsWith(".*") && matchingPackageRule(rule, className)) {
                if (DEBUG) {
                    Log.d("排除类：" + className);
                }
                // 处理当前包
                return false;
            } else if (matchingFullRule(rule, className)) {
                if (DEBUG) {
                    Log.d("排除类：" + className);
                }
                // 处理特定类
                return false;
            }
        }

        return true;
    }

    private static boolean matchingFullRule(String rule, String className) {
        return className.startsWith(rule);
    }

    private static boolean matchingPackageRule(String rule, String className) {
        rule = rule.replace("*", "");
        if (className.startsWith(rule)) {
            return className.replace(rule, "").indexOf(".") <= 0;
        }
        return false;
    }

    private static boolean matchingAllRule(String rule, String className) {
        rule = rule.replace("**", "");
        return className.startsWith(rule);
    }

    private static String typeToClassName(String type) {
        if (!type.startsWith("L")) {
            return type;
        }
        return type.substring(1).replace("/", ".").replace(";", "");
    }
}
