@echo off

:: 创建bin目录
if not exist bin mkdir bin

:: 编译所有Java文件
javac -d bin src\com\socialnetwork\**\*.java

:: 检查编译是否成功
if %errorlevel% equ 0 (
    echo 编译成功！
    echo 启动社交网络系统...
    :: 启动界面选择器
    java -cp bin com.socialnetwork.gui.Launcher
) else (
    echo 编译失败，请检查错误信息。
    pause
)