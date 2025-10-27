@echo off
chcp 65001 >nul
echo - компиляция java-проекта! -

if exist target (
    echo очистка папки target...
    rmdir /s /q target
)
mkdir target

echo поиск исходных файлов...
dir /s /b src\*.java > sources.txt

echo компиляция...
javac -d target -encoding UTF-8 -Xlint:unchecked @sources.txt

set COMPILE_ERROR=%errorlevel%
del sources.txt 2>nul

if not %COMPILE_ERROR% equ 0 (
    echo.
    echo ❌ ошибки!
    echo проверь код и попробуй ещё раз.
    
    pause >nul
    exit
)

echo ✅ успешно!
pause >nul
cls

echo - запуск программы. -
set MAIN_CLASS=Main

echo - класс: %MAIN_CLASS% -
echo.

java -cp target %MAIN_CLASS%

echo.
echo - программа завершена. -
pause >nul