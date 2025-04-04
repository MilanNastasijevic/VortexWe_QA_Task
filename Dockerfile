FROM maven:3.9.4-eclipse-temurin-17

# Install essential tools and Playwright browser system dependencies
RUN apt-get update && apt-get install -y \
    wget curl unzip gnupg coreutils \
 && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY pom.xml /app/
RUN mvn dependency:resolve

# Install all native system dependencies required by Playwright browsers
RUN mvn exec:java -e \
    -Dexec.mainClass=com.microsoft.playwright.CLI \
    -Dexec.args="install-deps"

COPY . /app

ENTRYPOINT ["mvn"]
