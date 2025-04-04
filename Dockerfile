FROM maven:3.9.4-eclipse-temurin-17

# Install essential tools
RUN apt-get update && apt-get install -y \
    wget curl unzip gnupg coreutils \
 && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy pom.xml and resolve dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Copy source and compile
COPY src ./src
RUN mvn compile

# Install system dependencies required by Playwright
RUN mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install-deps"

# Copy the rest of the project
COPY . .

ENTRYPOINT ["mvn"]
