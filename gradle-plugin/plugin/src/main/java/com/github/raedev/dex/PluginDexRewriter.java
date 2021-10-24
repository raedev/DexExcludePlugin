package com.github.raedev.dex;

import org.jf.dexlib2.rewriter.DexRewriter;

/**
 * @author RAE
 * @date 2021/10/2021/10/23
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PluginDexRewriter extends DexRewriter {

    private final static PluginRewriterModule DEFAULT_MODULE = new PluginRewriterModule();

    public PluginDexRewriter() {
        super(DEFAULT_MODULE);
    }
}
