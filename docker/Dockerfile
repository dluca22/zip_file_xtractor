## Multi stage production release

# Step 1 build container
  # use the same image of the dev container tagging it as builder for the step 2 reference
  # don't care about env variables, ownership, just get the java project related files
  # run package command, passing in option to skip tests initial phase of building
FROM maven:3.8.3-openjdk-17 AS builder
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests  


# Step 2 release release container (slim runtime image)
FROM eclipse-temurin:17-jre-jammy

ARG UID=1000
ARG GID=1000

RUN groupadd -g $GID appuser \
&& useradd -m -u $UID -g $GID -s /bin/bash appuser
USER appuser

WORKDIR /app
COPY --from=builder /workspace/target/zip_stl_xtractor-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
