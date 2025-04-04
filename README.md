#  VortexWe QA Engineer Task – Jira Clone Testing

This repository contains E2E tests for a Jira Clone app built with Angular and Akita. The framework used is **Playwright + TestNG**, and the project supports execution via **Docker**, with integrated **Allure reporting**.

##  Dependencies

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
### Option 4: Run with docker

```bash
mvn clean test -Dtest=IssueCreateJira#changeTileStatusTest
```
This builds and runs the test suite in a containerized environment.

docker-compose up --build
```bash
docker run --rm -it playwright-tests clean test
```
---

##  Test Details

* Tests are written using **TestNG** and use a shared `@DataProvider` to run across **Chromium**, **Firefox**, and **WebKit**.
* Browser lifecycle and context management handled via `BrowserManager.java`
* Test class: `IssueCreateJira.java`
* Supports thread-safe execution with parallel browser test runs

---

##  Allure Report

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

---

## 🚀 Run in GitHub Actions CI (with Docker)

A preconfigured GitHub Actions workflow is recommended to automate test runs and publish Allure reports.


### 2. View the Allure report
Once deployed, your Allure report will be live at:
```
https://<your-username>.github.io/<your-repo>/
```
