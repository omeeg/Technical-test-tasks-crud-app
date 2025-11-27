# Setup

This project is intended for Java Version 21.

To setup production environment:
from the root of this repository.

### 1. Build project .jar

`./mvnw clean install -DskipTests`

### 2. Build docker containers

` docker compose build`

### 3. Start containers

`docker compose up`
