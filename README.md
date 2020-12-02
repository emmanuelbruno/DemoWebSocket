# Demo Web Socket

## Objectives
A simple Java Websocket Server and Client with a minimal javascript web client.

## Building
```shell script
mvn clean package
```

## Running
### Java
By default the server and the client bind on localhost:8025

Run the server
```shell script
java -jar Server/target/Server-2.0-jar-with-dependencies.jar
```

Run one or mode clients
```shell script
java -jar JavaClient/target/JavaClient-2.0-jar-with-dependencies.jar
```

Run the web client in a jetty server 
```shell script
mvn --projects WebClient jetty:run-war
```
### Docker
Launch the server with a name and mapped port.
```shell script
docker run \
        --rm -it \
        -p 8025:8025 \
        --name wsserver \
        docker.io/brunoe/demowebsocket.server:
```

Launch one or more client linked to the server.
```shell script
docker run \
        --rm -it \
        --link wsserver \
        --env JAVA_OPTS="-Dfr.univtln.bruno.demo.websocket.server.ip=wsserver" \
        docker.io/brunoe/demowebsocket.javaclient:03cecac
```

Launch one or more web clients and open http://localhost:8080 (or map another port).
Warning, the client is Javascript executed in the browser, so the web socket server must 
be reachable from there and not from the container (so it needs to be mapped to an host port). 
```shell script
docker run \
        --link wsserver \
        --rm \
        -p 8080:8080 \
        brunoe/demowebsocket.webclient:03cecac
```