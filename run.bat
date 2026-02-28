@echo off
REM First, try using the standard 'java' command (This works for your friends who have Java in their PATH)
java -version >nul 2>&1
if %errorlevel% equ 0 (
    java FileEncryptor
    pause
    exit /b
)

REM If standard 'java' fails, use your specific local path as a fallback (This works for your computer)
if exist "C:\Program Files\Java\jre1.8.0_481\bin\java.exe" (
    "C:\Program Files\Java\jre1.8.0_481\bin\java.exe" FileEncryptor
) else (
    echo Java was not found on your system. Please install Java from java.com.
)
pause
