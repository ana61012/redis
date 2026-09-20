# Redis Server in Java

A Redis-compatible server built from scratch in Java.

## Features
- TCP server on port 6379
- Handles multiple concurrent clients
- Supports PING (more commands in progress)

## Run
mvn package
java -jar target/redis-java.jar

## Test
redis-cli ping

## Acknowledgements
Built using the CodeCrafters "Build Your Own Redis" challenge as a learning resource.
