# 🧪 Java + Playwright + TestNG + Allure Framework

## 📦 Dependencies

To run the tests, the following dependencies are required:

* **Java 17+** – JDK for compiling and running tests
* **Maven** – Build automation and dependency management
* **Playwright for Java** – Cross-browser automation engine
* **TestNG** – Testing framework with parallel execution
* **Allure TestNG** – Allure adapter for TestNG reports

These dependencies are handled automatically via `pom.xml`.

> ⚠️ **Note:** While smaller Docker images like Alpine reduce image size, they are not recommended for Playwright tests due to missing system libraries (like `glibc`). Use Debian- or Ubuntu-based images (like `maven:3.9.4-eclipse-temurin-17`) for full compatibility with Playwright.

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

## 🚀 Run in GitHub Actions CI (with Docker)

A preconfigured GitHub Actions workflow is recommended to automate test runs and publish Allure reports.

### 1. `.github/workflows/tests.yml`:

```yaml
name: Run Playwright Tests with Docker

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

      - name: Build Docker image
        run: docker build -t playwright-tests .

      - name: Run tests in container
        run: |
          docker run --rm \
            -v ${{ github.workspace }}/target:/app/target \
            playwright-tests test

      - name: Generate Allure report
        run: ./generate-allure-report.sh

      - name: Upload Allure report
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

> ❗️If WebKit tests fail on GitHub Actions with missing dependencies (e.g. `libgtk-4.so.1`), make sure your Docker image includes Playwright browser system dependencies.

You can do this in your `Dockerfile` by installing:

```Dockerfile
RUN apt-get update && apt-get install -y \
    libgtk-4-1 libgtk-3-0 libnss3 libx11-xcb1 libxcomposite1 \
    libxdamage1 libxrandr2 libgbm1 libxss1 libasound2 \
    libatk1.0-0 libatk-bridge2.0-0 libxinerama1 libxext6 \
    libxfixes3 libxrender1 libxcb1 libx11-6
```

Or, for Node environments:
```bash
npx playwright install-deps
```

Ensure this is run before test execution.

