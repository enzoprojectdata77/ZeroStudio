// 假设这是在 ToolsManager.java 内部，或者任何可以访问 ToolsManager 和 Environment 的地方

/**
 * 示例：演示各类文件操作场景
 * @author android_zero
 */
private void performInstallationExamples() {

    // ============================================================
    // 场景 1: 普通文件复制 (Single File Copy)
    // ============================================================
    // 目标：将 assets/scripts/init.sh 复制到 App 的 bin 目录
    // 参数含义：
    // 1. "scripts/init.sh" -> Assets 中的源路径
    // 2. Environment.BIN_DIR -> 目标文件夹
    // 3. false -> 不解压 (只是复制)
    // 4. 0 -> 深度无效 (不解压时忽略此参数)
    // 效果：如果目标存在init.sh，会被覆盖；如果不存在则创建。
    ToolsManager.installAsset(
        "scripts/init.sh",
        Environment.BIN_DIR,
        false,
        0
    );


    // ============================================================
    // 场景 2: 文件夹整体复制 (Directory Copy)
    // ============================================================
    // 目标：将 assets/python/lib 文件夹下的所有内容复制到 App 的 python 目录
    // 参数含义：
    // 1. "python/lib" -> Assets 中的源文件夹
    // 2. Environment.PYTHON_HOME -> 目标文件夹
    // 3. false -> 不解压
    // 4. 0 -> 深度无效
    // 效果：
    // - 如果目标文件夹已存在，则**跳过**（不进行任何操作，防止覆盖用户修改）。
    // - 如果目标不存在，递归创建目录并复制所有文件。
    ToolsManager.installAsset(
        "python/lib",
        Environment.PYTHON_HOME,
        false,
        0
    );


    // ============================================================
    // 场景 3: 简单 ZIP 解压 (Standard Unzip)
    // ============================================================
    // 目标：解压 busybox.zip 到 bin 目录
    // 压缩包结构：busybox.zip -> [busybox, ssl_helper] (无根文件夹)
    // 参数含义：
    // 1. "tools/busybox.zip" -> Assets 路径
    // 2. Environment.BIN_DIR -> 目标文件夹
    // 3. true -> 执行解压
    // 4. 0 -> 保持原有目录结构 (不剥离路径)
    ToolsManager.installAsset(
        "tools/busybox.zip",
        Environment.BIN_DIR,
        true,
        0
    );


    // ============================================================
    // 场景 4: 解压并去除顶层文件夹 (Unzip with Strip Depth)
    // ============================================================
    // 目标：解压 java-format.zip，但不要压缩包里的顶层文件夹名
    // 压缩包结构：java-format.zip -> java-format-1.0/bin/java-format
    // 我们希望结果是：Environment.BIN_DIR/bin/java-format (去掉 java-format-1.0/)
    // 参数含义：
    // 3. true -> 解压
    // 4. 1 -> 剥离第 1 层目录 (即去掉 java-format-1.0/)
    ToolsManager.installAsset(
        "plugins/java-format.zip",
        Environment.BIN_DIR,
        true,
        1
    );


    // ============================================================
    // 场景 5: 解压特定文件夹 (Filter / Partial Extract)
    // ============================================================
    // 目标：从巨大的 clang.zip 中，只解压 include 头文件
    // 压缩包结构：clang.zip -> [bin/, lib/, include/, share/]
    // 参数含义：
    // 5. "include/" -> 过滤器，只解压路径以 "include/" 开头的文件
    ToolsManager.installAsset(
        "tools/clang.zip",
        Environment.CLANG_HOME,
        true,
        0,
        "include/"
    );


    // ============================================================
    // 场景 6: 高级操作 - 筛选并剥离路径 (Filter + Strip)
    // ============================================================
    // 目标：从 ndk.zip 中提取 arm64 架构的库，并把它们直接放到 lib 根目录
    // 压缩包结构：ndk.zip -> toolchains/llvm/prebuilt/lib/clang/17/lib/linux/aarch64/libomp.so
    // 目标路径：Environment.LIB_DIR/libomp.so
    // 参数含义：
    // 4. 9 -> 剥离深度，需要数一下上面路径里有多少层文件夹要去掉
    //    (toolchains/llvm/prebuilt/lib/clang/17/lib/linux/aarch64/ = 9层)
    // 5. "toolchains/llvm/.../aarch64/" -> 指定只解压这个深层目录下的文件
    String innerPath = "toolchains/llvm/prebuilt/lib/clang/17/lib/linux/aarch64/";
    ToolsManager.installAsset(
        "tools/ndk_libs.zip",
        Environment.LIB_DIR,
        true,
        9, // 剥离掉 innerPath 指定的那 9 层目录，直接把 .so 文件释放到 LIB_DIR
        innerPath
    );
}

/*
 * This file is part of AndroidIDE.
 * ... (License header) ...
 */
package com.itsaky.androidide.managers;

// ... (Imports) ...

public class ToolsManager {

  // ... (Logger and Constants) ...

  public static void init(@NonNull BaseApplication app, Runnable onFinish) {

    if (!IDEBuildConfigProvider.getInstance().supportsCpuAbi()) {
      LOG.error("Device not supported");
      return;
    }

    // 在异步线程中执行，避免阻塞主线程
    CompletableFuture.runAsync(() -> {
      // 1. 原有的初始化逻辑
      IJdkDistributionProvider.getInstance().loadDistributions();

      writeNoMediaFile();
      extractAapt2();
      extractToolingApi();
      extractAndroidJar();
      extractColorScheme(app);
      writeInitScript();

      // -----------------------------------------------------------
      // 2. 在这里调用我们新增的工具解压逻辑
      // -----------------------------------------------------------
      installExtraTools(); 

      deleteIdeenv();
    }).whenComplete((__, error) -> {
      if (error != null) {
        LOG.error("Error extracting tools", error);
      }

      if (onFinish != null) {
        onFinish.run();
      }
    });
  }

  /**
   * 专门用于集中管理 Assets 解压/安装的私有方法
   * 这里展示了 installAsset 的各种用法
   */
  private static void installExtraTools() {
      // 示例 1: 安装 Python 环境 (解压 ZIP)
      // 场景: assets/python.zip -> 解压到 Environment.PYTHON_HOME
      // 参数: isUnzip=true, depth=0 (保持压缩包原有结构)
      installAsset(
          "tools/python.zip", 
          Environment.PYTHON_HOME, 
          true, 
          0
      );

      // 示例 2: 安装 Maven (解压 ZIP 并去除顶层目录)
      // 场景: assets/maven.zip (内部是 apache-maven-3.9/bin/...) -> 目标 Environment.MAVEN_HOME/bin/...
      // 参数: isUnzip=true, depth=1 (去掉第一层 apache-maven-3.9 文件夹)
      installAsset(
          "tools/maven.zip", 
          Environment.MAVEN_HOME, 
          true, 
          1
      );

      // 示例 3: 复制初始化脚本 (单文件复制)
      // 场景: assets/scripts/init.sh -> 复制到 Environment.BIN_DIR
      // 参数: isUnzip=false (直接复制), depth=0 (无效参数)
      // 注意: 如果文件存在会强制覆盖
      installAsset(
          "scripts/init.sh", 
          Environment.BIN_DIR, 
          false, 
          0
      );

      // 示例 4: 复制整个配置文件夹 (文件夹复制)
      // 场景: assets/conf/nginx -> 复制到 Environment.DATA_DIR/nginx
      // 参数: isUnzip=false
      // 注意: 如果目标文件夹已存在，会直接跳过，保留用户配置
      installAsset(
          "conf/nginx", 
          new File(Environment.DATA_DIR, "nginx"), 
          false, 
          0
      );

      // 示例 5: 高级解压 - 提取特定架构的库 (过滤 + 深度剥离)
      // 场景: 从大包 libs.zip 中，只提取 lib/arm64-v8a/ 下的文件到 Environment.LIB_DIR
      // 参数: filter="lib/arm64-v8a/", depth=2 (去掉 lib/ 和 arm64-v8a/ 这两层)
      installAsset(
          "tools/libs.zip", 
          Environment.LIB_DIR, 
          true, 
          2,
          "lib/arm64-v8a/"
      );
  }
}