FROM maven:3.9.9-eclipse-temurin-21 AS tester

WORKDIR /workspace

COPY chimera-agent/chimera-agent /workspace/chimera-agent/chimera-agent

WORKDIR /workspace/chimera-agent/chimera-agent

RUN chmod +x mvnw

CMD ["./mvnw", "test"]
