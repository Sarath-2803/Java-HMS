javac -cp "lib/postgresql-42.7.8.jar" -d bin $(find src -name "*.java")
java -cp "bin:lib/postgresql-42.7.8.jar" Main