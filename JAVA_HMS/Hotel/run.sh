#!/bin/bash

# Set environment variables
export DB_USER=postgres
export DB_PASSWORD=postgres
export DB_URL=jdbc:postgresql://localhost:5432/test

# Create bin folder if it doesn't exist
mkdir -p bin

# Compile and run
javac -cp "lib/postgresql-42.7.8.jar" -d bin $(find src -name "*.java")
java -cp "bin:lib/postgresql-42.7.8.jar" Main