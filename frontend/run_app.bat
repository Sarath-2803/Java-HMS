@echo off
REM Restaurant Reservation System - Desktop Application Launcher
REM This script runs the Hotel Management System as a desktop application
REM No need to start the backend server - it connects directly to the database

echo Starting Hotel Management System...
echo.
echo Make sure PostgreSQL is running on localhost:5432
echo Database: test
echo.

cd /d "%~dp0frames"

java -cp ".;..\..\backend\lib\postgresql-42.7.1.jar;..\..\backend\build" login

echo.
echo Application closed.
pause
