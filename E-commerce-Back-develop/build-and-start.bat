@echo off
setlocal

:: Get the directory of this script
set "SCRIPT_DIR=%~dp0"

:: List of folders
set FOLDERS=API-Gateway Cart config eureka Inventory Payment Product Messaging

:: Loop through each folder and run the Maven command
for %%F in (%FOLDERS%) do (
    echo.
    echo ===== Processing %%F =====
    pushd "%SCRIPT_DIR%%%F"
    call mvn clean package -DskipTests
    if errorlevel 1 (
        echo [ERROR] Maven build failed in %%F. Aborting script.
        popd
        exit /b 1
    )
    popd
)

echo.
echo === Reached end of loop. Preparing to run docker-compose...

echo ===== Maven builds complete! Starting Docker Compose... =====

:: Run docker-compose up -d from the same folder as the script
pushd "%SCRIPT_DIR%"
docker-compose up -d
popd

echo.
echo ===== All done! =====
endlocal