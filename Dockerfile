# ---------- Stage 1: Build + install deps ----------
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Install core tools required for downloading, extracting, and Allure CLI setup
RUN apt-get update && apt-get install -y \
    wget curl unzip gnupg coreutils \
 && apt-get clean && rm -rf /var/lib/apt/lists/*

# Cache Maven dependencies
COPY pom.xml ./
RUN mvn dependency:resolve

# Add source code and compile it
COPY src ./src
RUN mvn compile

# Install Playwright browser dependencies using CLI
RUN mvn exec:java -e \
  -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install-deps"

# -Stage 2: Runtime-only (lean and clean)
FROM maven:3.9.4-eclipse-temurin-17 AS runtime

WORKDIR /app

# Copy compiled code and resolved dependencies
COPY --from=builder /app /app

# Optional: Copy Allure CLI if you installed it in builder
# COPY --from=builder /opt/allure /opt/allure
# RUN ln -s /opt/allure/allure-2.24.0/bin/allure /usr/local/bin/allure

ENTRYPOINT ["mvn"]
