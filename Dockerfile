FROM maven:3.9.4-eclipse-temurin-17

WORKDIR /app
COPY . /app

# Optional: install Allure CLI manually if needed
RUN apt-get update && \
    apt-get install -y unzip curl && \
    curl -Lo allure.zip https://github.com/allure-framework/allure2/releases/download/2.24.0/allure-2.24.0.zip && \
    unzip allure.zip -d /opt/allure && \
    ln -s /opt/allure/allure-2.24.0/bin/allure /usr/local/bin/allure

ENTRYPOINT ["mvn"]