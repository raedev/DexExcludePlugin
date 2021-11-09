package com.github.raedev.dex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RAE
 * @date 2021/10/2021/10/23
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class DexExcludeExtension {

    // exclude 和 include 只能作用其中一个

    private List<String> mExcludes = new ArrayList<>();
    private List<String> mIncludes = new ArrayList<>();

    public void exclude(String rule) {
        if (!mExcludes.contains(rule)) {
            mExcludes.add(rule);
        }
    }

    public void include(String rule) {
        if (!mExcludes.contains(rule)) {
            mExcludes.add(rule);
        }
    }

    public void excludes(List<String> values) {
        mExcludes.addAll(values);
    }

    public void includes(List<String> values) {
        mIncludes.addAll(values);
    }

    public List<String> getExcludes() {
        return mExcludes;
    }

    public List<String> getIncludes() {
        return mIncludes;
    }

    public void debug(boolean value) {
        Log.DEBUG = value;
    }

}
