# DexExcludePlugin

[![](https://jitpack.io/v/raedev/DexExcludePlugin.svg)](https://jitpack.io/#raedev/DexExcludePlugin)

`DexExcludePlugin` 是一个用于排除.dex文件中指定class类的gradle插件工具

## 使用

1、在根目录的`build.gradle`中配置插件

```groovy
buildscript {
    repositories {
        google()
        mavenCentral()
        // 使用jitpack仓库
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:7.0.3'
        // 配置插件
        classpath 'com.github.raedev:DexExcludePlugin:1.0.0'
    }
}
```

2、在项目的`build.gradle`中配置

> 排除模式

排除指定包的类

```groovy
apply plugin: 'com.github.raedev.plugin'
dexExcludePlugin {
    // 是否开启调试输出
    debug false
    // 第一种配置方式
    excludes = [
            'androidx'
    ]
    // 第二种配置规则
    exclude 'androidx'
    exclude 'android.support.v4'
    exclude 'com.google.android.material'
}
```

> 包含模式

只打包配置的类，跟上述`exclude`的模式相互排斥，两种方式只能采用其中一种。

```groovy
apply plugin: 'com.github.raedev.plugin'
dexExcludePlugin {
    // 是否开启调试输出
    debug false
    // 第一种配置方式
    includes = [
            'androidx'
    ]
    // 第二种配置规则
    include 'androidx'
    include 'android.support.v4'
    include 'com.google.android.material'
}
```

## 执行任务

当触发构建的时候，比如：`:app:assembleDebug`、：`:app:assembleRelease` 自动执行插件。 或者自行执行任务：

```text
Task
   |-- dex
       |-- dexClassExcludeDebug
       |__ dexClassExcludeRelease
```

## 配置规则

示例解析：

- `androidx`：排除androidx开头的类
- `com.android.*`： 排除com.android包下的类
- `com.android.**`：排除com.android包下的类以及子包所有类

## 示例

> 匹配规则

```groovy
exclude 'androidx'
exclude 'android.support.v4'
```

> 产生结果

| Before        | After   | 
| :--------:   | :-----:  | 
| ![](https://github.com/raedev/DexExcludePlugin/raw/master/pics/before.png)  | ![](https://github.com/raedev/DexExcludePlugin/raw/master/pics/after.png)
