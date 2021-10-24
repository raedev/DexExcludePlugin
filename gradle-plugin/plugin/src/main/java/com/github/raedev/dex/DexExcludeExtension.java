package com.github.raedev.dex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RAE
 * @date 2021/10/2021/10/23
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class DexExcludeExtension {

    List<String> excludes = new ArrayList<>();

    public void exclude(String rule) {
        if (!excludes.contains(rule)) {
            excludes.add(rule);
        }
    }

    public void debug(boolean value) {
        ExcludeClassHelper.DEBUG = value;
    }

}
