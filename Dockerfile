# Stage 1: Builder with full dependencies and Allure install
FROM maven:3.9.4-eclipse-temurin-17 as builder

RUN apt-get update && apt-get install -y \
    libglib2.0-0 \
    libnss3 \
    libnspr4 \
    libdbus-1-3 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libatspi2.0-0 \
    libx11-6 \
    libxcomposite1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libxcb1 \
    libxkbcommon0 \
    libasound2 \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*

RUN curl -Lo allure.zip https://github.com/allure-framework/allure2/releases/download/2.24.0/allure-2.24.0.zip && \
    unzip allure.zip -d /opt/allure && \
    rm allure.zip

# Stage 2: Runtime with copied tools
FROM maven:3.9.4-eclipse-temurin-17

COPY --from=builder /opt/allure /opt/allure
RUN ln -s /opt/allure/allure-2.24.0/bin/allure /usr/local/bin/allure

WORKDIR /app
COPY . /app

ENTRYPOINT ["mvn"]
