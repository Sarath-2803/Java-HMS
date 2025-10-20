#!/bin/bash

# Set environment variables
export MIG_DIR="/home/sarath/Projects/oop/Java-HMS/JAVA_HMS/Hotel/resources/db/migrations/"; # Set your migrations directory
export DB_USER=postgres     # Set your database username
export DB_PASSWORD=postgres  # Set your database password
export DB_URL=jdbc:postgresql://localhost:5432/test  # Set your database URL

# Create bin folder if it doesn't exist
mkdir -p bin

# Compile and run
javac -cp "lib/postgresql-42.7.8.jar" -d bin $(find src -name "*.java")
java -cp "bin:lib/postgresql-42.7.8.jar" Main