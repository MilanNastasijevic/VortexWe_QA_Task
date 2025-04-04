# 🧪 Java + Playwright + TestNG + Allure Framework

## 📦 Dependencies

To run the tests, the following dependencies are required:

* **Java 17+** – JDK for compiling and running tests
* **Maven** – Build automation and dependency management
* **Playwright for Java** – Cross-browser automation engine
* **TestNG** – Testing framework with parallel execution
* **Allure TestNG** – Allure adapter for TestNG reports

These dependencies are handled automatically via `pom.xml`.

---

## ⚙️ Installation

1. **Clone the repository:**

```bash
git clone <url-of-repository>
cd <project-folder>
```

2. **Install dependencies and build:**

```bash
mvn clean install
```

This will download all Maven dependencies and compile the project.

---

## 🚀 Running the Tests

### Option 1: Run all tests across all browsers

```bash
mvn clean test
```

### Option 2: Run tests for a single browser (Chromium)

```bash
mvn clean test -Dbrowser=chromium
```

### Option 3: Run specific test method

```bash
mvn clean test -Dtest=IssueCreateJira#changeTileStatusTest
```

---

## 🧪 Test Details

* Tests are written using **TestNG** and use a shared `@DataProvider` to run across **Chromium**, **Firefox**, and **WebKit**.
* Browser lifecycle and context management handled via `BrowserManager.java`
* Test class: `IssueCreateJira.java`
* Supports thread-safe execution with parallel browser test runs

---

## 📊 Allure Report

### Generate and view the report:

```bash
./generate-allure-report.sh
```

This script will:
- Merge all `allure-results-*` folders into `target/allure-results`
- Auto-generate `environment.properties` and `executor.json`
- Build the report to `target/allure-report`
- Open it in your browser

### Sample Report Includes:
- Test run grouping per browser
- Executor information (Local, GitHub Actions, GitLab)
- Environment details (URL, Browser list, Mode)

> The `environment.properties` file is generated dynamically by the framework and does not need to be committed or manually created in `src/test/resources`. It is written directly into `target/allure-results`.

---

## 🚀 Run in GitHub Actions CI

A preconfigured GitHub Actions workflow is recommended to automate test runs and publish Allure reports.

### 1. Add `.github/workflows/tests.yml`:

```yaml
name: Run Playwright + TestNG Tests

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v3

      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Cache Maven packages
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}

      - name: Build and test
        run: mvn clean test

      - name: Generate Allure report
        run: ./generate-allure-report.sh

      - name: Upload Allure report as artifact
        uses: actions/upload-artifact@v4
        with:
          name: allure-report
          path: target/allure-report

  deploy:
    needs: test
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'

    steps:
      - name: Download Allure report artifact
        uses: actions/download-artifact@v4
        with:
          name: allure-report
          path: public

      - name: Deploy Allure report to GitHub Pages
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./public
```

### 2. View the Allure report
Once deployed, your Allure report will be live at:
```
https://<your-username>.github.io/<your-repo>/
```

---

## 🐳 Dockerfile for GitHub Actions or Local Runs

```Dockerfile
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
```

To build and run tests in Docker:
```bash
docker build -t testng-playwright .
docker run --rm testng-playwright clean test
```

---

## 📁 Project Structure

```
project-root/
├── src/
│   └── test/
│       ├── java/
│       │   ├── Jira/
│       │   │   └── IssueCreateJira.java
│       │   └── utils/
│       │       ├── BrowserManager.java
│       │       └── AllureMetadataWriter.java
│       └── resources/
│           └── testng.xml
├── target/
│   └── allure-results-*/
│   └── allure-results/
│   └── allure-report/
├── .github/workflows/tests.yml
├── generate-allure-report.sh
├── Dockerfile
├── pom.xml
└── README.md
```

---

## 🧼 Additional Features

- ✅ `environment.properties` auto-generated with browser and env info
- ✅ `executor.json` shows CI context (Jenkins, GitHub, GitLab, or Local)
- ✅ `generate-allure-report.sh` merges results and opens report
- ✅ Cross-browser test handling
- ✅ CI-ready (GitHub Actions + GitHub Pages publishing supported)
- ✅ One-line test + report automation on every push/PR

---

Want to expand this? Options include:
- CLI filtering by tag or custom mode
- Slack notifications
- Custom test matrix via GitHub Actions

