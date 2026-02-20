FROM docker.io/library/eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /src/eshop
COPY . .

# Windows-safe: fix CRLF + ensure gradlew executable
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

RUN ./gradlew clean bootJar

FROM docker.io/library/eclipse-temurin:21-jre-alpine AS runner

ARG USER_NAME=eshop
ARG USER_UID=1000
ARG USER_GID=${USER_UID}

RUN addgroup -g ${USER_GID} ${USER_NAME} \
 && adduser -D -h /opt/eshop -u ${USER_UID} -G ${USER_NAME} ${USER_NAME}

USER ${USER_NAME}
WORKDIR /opt/eshop

COPY --from=builder --chown=${USER_UID}:${USER_GID} /src/eshop/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java -Dserver.port=$PORT -jar app.jar"]
CMD ["-jar", "app.jar"]