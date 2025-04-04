FROM maven:3.9.4-eclipse-temurin-17

# Install required system packages for Maven & Playwright
RUN apt-get update && apt-get install -y \
    curl unzip gnupg coreutils \
 && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy project files and resolve dependencies
COPY pom.xml ./
COPY src ./src
COPY . .
RUN mvn clean compile

# ✅ Install Playwright's native dependencies
RUN mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install-deps"

# Run tests (Allure metadata will be written from @AfterSuite hook)
ENTRYPOINT ["mvn", "test"]
