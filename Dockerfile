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

# Install Playwright system dependencies (native libs for Chromium, Firefox, WebKit)
RUN apt-get update && apt-get install -y \
    wget curl unzip gnupg ca-certificates \
    libglib2.0-0 libnss3 libnspr4 libatk1.0-0 libatk-bridge2.0-0 \
    libx11-6 libxcomposite1 libxdamage1 libxext6 libxfixes3 libxrandr2 \
    libgbm1 libxcb1 libxkbcommon0 libasound2 libgtk-3-0 libxshmfence1 \
    libxrender1 libxcursor1 libxi6 libxtst6 libxss1 libwayland-client0 \
    libwayland-server0 libatspi2.0-0 libjpeg-dev libxinerama1 libpangocairo-1.0-0 \
    libpango-1.0-0 libharfbuzz0b libcairo2 libgdk-pixbuf2.0-0 libdrm2 \
    libopus0 libevent-2.1-7 libvpx7 libflite1 libenchant-2-2 libx11-xcb1 \
 && apt-get clean && rm -rf /var/lib/apt/lists/*

# Run tests (Allure metadata will be written from @AfterSuite hook)
ENTRYPOINT ["mvn", "test"]
