package com.github.raedev.dex;

import java.io.File;

/**
 * @author RAE
 * @date 2021/10/2021/10/22
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
final class Log {

    public static File BUILD_DIR;
    public static boolean DEBUG = false;

    public static void d(String msg) {
        if (Log.DEBUG) {
            System.out.println("> RAE.DexExclude: " + msg);
        }
    }

    public static void i(String msg) {
        System.out.println("> RAE.DexExclude: " + msg);
    }

    public static void e(String msg) {
        System.err.println("> RAE.DexExclude: " + msg);
    }

    public static void e(String msg, Throwable e) {
        System.err.println("> RAE.DexExclude: " + msg);
    }

}
