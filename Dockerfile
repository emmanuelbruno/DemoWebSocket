# syntax=docker/dockerfile:experimental

## WARNING This build requires experimental features (native build and buildkit)
## run with the following command
## COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose up --build

## If needed invalidate the cache with
#docker builder prune --filter type=exec.cachemount

ARG JRE=adoptopenjdk/openjdk11:jre-11.0.9_11-alpine

FROM maven:3.6.3-jdk-11-openj9 as BUILD
WORKDIR /build
COPY . /build
RUN --mount=type=cache,target=/root/.m2 mvn -B -e -T 1C verify

FROM ${JRE} as Server
COPY --from=BUILD /build/Server/target/*-jar-with-dependencies.jar /MyApp.jar
ENTRYPOINT java $JAVA_OPTS -jar /MyApp.jar

FROM ${JRE} as JavaClient
COPY --from=BUILD /build/JavaClient/target/*-jar-with-dependencies.jar /MyApp.jar
ENTRYPOINT java $JAVA_OPTS -jar /MyApp.jar

FROM jetty:9.4.35-jre11-slim as WebClient
COPY --from=BUILD /build/WebClient/target/*.war /var/lib/jetty/webapps/root.war