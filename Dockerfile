FROM maven:3.9.4-eclipse-temurin-17

# Install dependencies needed by Playwright browsers
RUN apt-get update && apt-get install -y \
    libgtk-4-1 libgtk-3-0 libnss3 libx11-xcb1 libxcomposite1 \
    libxdamage1 libxrandr2 libgbm1 libxss1 libasound2 \
    libatk1.0-0 libatk-bridge2.0-0 libxinerama1 libxext6 \
    libxfixes3 libxrender1 libxcb1 libx11-6 \
    && apt-get clean

WORKDIR /app
COPY . /app

# Optional: install Allure CLI
RUN curl -Lo allure.zip https://github.com/allure-framework/allure2/releases/download/2.24.0/allure-2.24.0.zip && \
    unzip allure.zip -d /opt/allure && \
    ln -s /opt/allure/allure-2.24.0/bin/allure /usr/local/bin/allure

ENTRYPOINT ["mvn"]

