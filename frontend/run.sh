#!/bin/bash
# Restaurant Reservation System - Desktop Application Launcher
# This script runs the Hotel Management System as a desktop application
# No need to start the backend server - it connects directly to the database

echo "Starting Hotel Management System..."
echo ""
echo "Make sure PostgreSQL is running on localhost:5432"
echo "Database: test"
echo ""

cd frames
java -cp ".:../../backend/lib/postgresql-42.7.1.jar:../../backend/build" login
