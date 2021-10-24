package com.github.raedev.dex;

import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.rewriter.ClassDefRewriter;
import org.jf.dexlib2.rewriter.DexFileRewriter;
import org.jf.dexlib2.rewriter.Rewriter;
import org.jf.dexlib2.rewriter.RewriterModule;
import org.jf.dexlib2.rewriter.Rewriters;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * @author RAE
 * @date 2021/10/2021/10/23
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class PluginRewriterModule extends RewriterModule {

    @Nonnull
    @Override
    public Rewriter<DexFile> getDexFileRewriter(@Nonnull Rewriters rewriters) {
        return new DexFileRewriter(rewriters) {
            @Nonnull
            @Override
            public DexFile rewrite(@Nonnull DexFile value) {
                return new RewrittenDexFile(value) {
                    @Nonnull
                    @Override
                    public Set<? extends ClassDef> getClasses() {
                        return ExcludeClassHelper.excludeClasses(super.getClasses());
                    }
                };
            }
        };
    }

    @Nonnull
    @Override
    public Rewriter<ClassDef> getClassDefRewriter(@Nonnull Rewriters rewriters) {
        return new ClassDefRewriter(rewriters) {
            @Nonnull
            @Override
            public ClassDef rewrite(@Nonnull ClassDef classDef) {
                return new RewrittenClassDef(classDef) {
                    @Nonnull
                    @Override
                    public List<String> getInterfaces() {
                        return ExcludeClassHelper.excludeInterfaces(super.getInterfaces());
                    }

                    @Nonnull
                    @Override
                    public Set<? extends Annotation> getAnnotations() {
                        return ExcludeClassHelper.excludeAnnotations(super.getAnnotations());
                    }
                };
            }

        };
    }
}
