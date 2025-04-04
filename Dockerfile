FROM maven:3.9.4-eclipse-temurin-17

# Install base tools
RUN apt-get update && apt-get install -y \
    wget curl unzip gnupg coreutils \
 && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml /app/
RUN mvn dependency:resolve

# Compile project to make sure classes are available
COPY src /app/src
RUN mvn compile

# Install all required system dependencies for Playwright
RUN mvn exec:java -e \
    -Dexec.mainClass=com.microsoft.playwright.CLI \
    -Dexec.args="install-deps"

# Copy the rest of the project
COPY . /app

ENTRYPOINT ["mvn"]
