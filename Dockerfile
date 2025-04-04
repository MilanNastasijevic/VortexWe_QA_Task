FROM maven:3.9.4-eclipse-temurin-17

# Install tools + make sure we're ready
RUN apt-get update && apt-get install -y \
    curl unzip gnupg coreutils \
 && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Prepare dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Copy and compile source to enable exec:java
COPY src ./src
RUN mvn compile

# ✅ Install Playwright dependencies
mvn compile exec:java \
  -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install-deps"

# Copy the rest (in case anything else changed)
COPY . .

ENTRYPOINT ["mvn"]
