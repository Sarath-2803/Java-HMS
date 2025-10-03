# Restaurant Reservation System - Desktop Application Launcher
# This script runs the Hotel Management System as a desktop application
# No need to start the backend server - it connects directly to the database

Write-Host "Starting Hotel Management System..." -ForegroundColor Green
Write-Host ""
Write-Host "Make sure PostgreSQL is running on localhost:5432" -ForegroundColor Yellow
Write-Host "Database: test" -ForegroundColor Yellow
Write-Host ""

# Change to the frontend frames directory
Set-Location -Path "$PSScriptRoot\frames"

# Run the application with correct classpath
java -cp ".;..\..\backend\lib\postgresql-42.7.1.jar;..\..\backend\build" login

Write-Host ""
Write-Host "Application closed." -ForegroundColor Cyan
