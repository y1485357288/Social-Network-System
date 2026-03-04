@echo off

REM 自动检测 Java 环境
if defined JAVA_HOME (
    echo 使用 JAVA_HOME: %JAVA_HOME%
) else (
    echo 未找到 JAVA_HOME，尝试使用系统默认 Java...
    where javac >nul 2>&1
    if %errorlevel% equ 0 (
        echo 已在系统 PATH 中找到 Java
    ) else (
        echo 错误：未找到 Java，请先安装 JDK 并配置环境变量
        pause
        exit /b 1
    )
)

REM 创建输出目录
if not exist out mkdir out

REM 编译所有 Java 文件
echo ========================================
echo 正在编译 Java 文件...
echo ========================================
javac -d out -encoding UTF-8 -cp out src\com\socialnetwork\model\User.java src\com\socialnetwork\core\AdjacencyList.java src\com\socialnetwork\core\HashTable.java src\com\socialnetwork\core\GraphAlgorithm.java src\com\socialnetwork\core\RecommendationEngine.java src\com\socialnetwork\data\DataLoader.java src\com\socialnetwork\gui\VisualizationPanel.java src\com\socialnetwork\gui\SocialNetworkApp.java

REM 检查编译是否成功
if %errorlevel% equ 0 (
    echo ========================================
    echo ✓ 编译成功！
    echo ========================================
    echo 正在启动社交网络系统...
    echo.
    java -cp out com.socialnetwork.gui.SocialNetworkApp
) else (
    echo ========================================
    echo ✗ 编译失败，请检查错误信息
    echo ========================================
    pause
)
