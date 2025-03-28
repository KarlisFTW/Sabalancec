@echo off
setlocal enabledelayedexpansion

:: Get the folder path from drag and drop
set "folderpath=%~1"

:: Check if a folder was provided
if "%folderpath%"=="" (
    echo No folder specified. Drag a folder onto this batch file.
    pause
    exit /b 1
)

:: Check if the path exists and is a directory
if not exist "%folderpath%\" (
    echo The specified path is not a valid directory.
    pause
    exit /b 1
)

echo Converting filenames to lowercase in: %folderpath%
echo.

:: Process all files in the folder
for %%F in ("%folderpath%\*.*") do (
    set "filename=%%~nxF"
    set "filepath=%%~dpF"
    
    :: Convert filename to lowercase
    for %%a in ("A=a" "B=b" "C=c" "D=d" "E=e" "F=f" "G=g" "H=h" "I=i" "J=j" "K=k" "L=l" "M=m" "N=n" "O=o" "P=p" "Q=q" "R=r" "S=s" "T=t" "U=u" "V=v" "W=w" "X=x" "Y=y" "Z=z") do (
        set "filename=!filename:%%~a!"
    )
    
    :: Rename the file if the name is different
    if not "%%~nxF"=="!filename!" (
        echo Renaming: "%%~nxF" to "!filename!"
        ren "%%F" "!filename!"
    )
)

echo.
echo All filenames converted to lowercase.
pause