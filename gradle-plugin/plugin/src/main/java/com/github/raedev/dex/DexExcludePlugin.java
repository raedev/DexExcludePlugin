/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.raedev.dex;

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension;
import com.android.build.gradle.internal.dsl.BuildType;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.rewriter.Rewriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * [Main] Exclude Dex Class Plugin
 * @author RAE
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "AlibabaUndefineMagicConstant"})
public class DexExcludePlugin implements Plugin<Project> {

    private final PluginDexRewriter mDexRewriter = new PluginDexRewriter();
    Project mProject;
    DexExcludeExtension mDexExcludeExtension;

    @Override
    public void apply(Project project) {
        Log.BUILD_DIR = project.getBuildDir();
        mDexExcludeExtension = project.getExtensions().create("dexExcludePlugin", DexExcludeExtension.class);
        File dexExcludeBackupDir = new File(Log.BUILD_DIR, "intermediates/dex_exclude_backup");
        if (!dexExcludeBackupDir.exists()) {
            dexExcludeBackupDir.mkdirs();
        }

        project.afterEvaluate(prj -> {
            mProject = prj;
            if (mDexExcludeExtension.getExcludes().size() <= 0 && mDexExcludeExtension.getIncludes().size() <= 0) {
                Log.e("a rules not found!");
                return;
            }
            BaseAppModuleExtension extension = prj.getExtensions().getByType(BaseAppModuleExtension.class);
            for (BuildType buildType : extension.getBuildTypes()) {
                registerTask(buildType.getName());
            }
        });

    }

    /**
     * 创建任务
     * @param name 构建类型
     */
    private void registerTask(String name) {
        // Register a task
        final String buildType = name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
        String taskName = String.format("dexExclude%s", buildType);
        mProject.getTasks().register(taskName, task -> {
            task.setGroup("dex");
            if ("debug".equalsIgnoreCase(buildType)) {
                task.dependsOn("mergeProjectDexDebug");
            } else {
                task.dependsOn("mergeDexRelease");
            }
            task.doLast(this::doTask);
        });

        // 打包时添加任务依赖
        mProject.getTasks().getByName("package" + buildType).dependsOn(taskName);
    }

    /**
     * 执行任务
     * @param task 任务
     */
    private void doTask(Task task) {
        // 初始化Helper
        ExcludeClassHelper.onConfig(this);
        String buildType = task.getName().replaceFirst("dexExclude", "");
        List<File> dexFiles = findDexFiles(buildType);
        if (dexFiles.size() <= 0) {
            Log.e("all dex files not found!");
            return;
        }
        for (String exclude : mDexExcludeExtension.getExcludes()) {
            Log.d("exclude rule：" + exclude);
        }
        for (File dexFile : dexFiles) {
            replaceDexFile(dexFile);
        }
    }

    /**
     * 替换Dex文件
     * @param file 源文件
     */
    private void replaceDexFile(File file) {
        String shortPath = toShortFile(file);
        try {
            Log.d("exclude dex file : " + shortPath);
            DexBackedDexFile loadDexFile = DexFileFactory.loadDexFile(file, Opcodes.getDefault());
            Rewriter<DexFile> rewriter = mDexRewriter.getDexFileRewriter();
            DexFile dexFile = rewriter.rewrite(loadDexFile);
            if (Log.DEBUG) {
                // 备份原来的dex文件
                String backup = file.getPath().replace(File.separator + "dex" + File.separator,
                        File.separator + "dex_exclude_backup" + File.separator);
                File backupDir = new File(backup).getParentFile();
                if (!backupDir.exists()) {
                    backupDir.mkdirs();
                }
                file.renameTo(new File(backup));
            }
            if (file.exists()) {
                file.delete();
            }
            DexFileFactory.writeDexFile(file.getPath(), dexFile);
            Log.i("exclude dex class finish: " + file.getPath());
        } catch (IOException e) {
            Log.e("exclude dex file error, path=" + shortPath + "; Error message: " + e.getMessage(), e);
        }
    }

    private String toShortFile(File file) {
        File buildDir = mProject.getBuildDir();
        return file.getPath().replace(buildDir.getPath(), "").replace("\\intermediates\\", "");
    }

    protected List<File> findDexFiles(String buildType) {
        File file = new File(mProject.getBuildDir(), "intermediates/dex/" + buildType.toLowerCase(Locale.ROOT));
        List<File> dexFiles = new ArrayList<>();
        findDexFiles(dexFiles, file);
        return dexFiles;
    }

    private void findDexFiles(List<File> result, File file) {
        if (file.isFile() && file.getName().endsWith(".dex")) {
            result.add(file);
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File item : files) {
            if (item.isDirectory()) {
                // 递归
                findDexFiles(result, item);
                continue;
            }
            if (item.isFile() && item.getName().endsWith(".dex")) {
                result.add(item);
            }
        }
    }
}
