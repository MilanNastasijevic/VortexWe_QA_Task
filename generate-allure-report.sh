#!/bin/bash

set -e

echo "🧹 Preparing merged Allure results folder..."
mkdir -p target/allure-results

echo "🔁 Merging browser-specific results..."
find target -type d -name "allure-results-*" | while read dir; do
  echo "→ Merging: $dir"
  cp -r "$dir"/* target/allure-results/ 2>/dev/null
done

echo "🧠 Re-writing metadata..."
mvn -q compile exec:java -Dexec.mainClass="testUtils.AllureMetadataWriter"

# ✅ Use a folder outside of target to avoid permission issues
REPORT_DIR="allure-report"

echo "📊 Generating Allure report to $REPORT_DIR"
rm -rf $REPORT_DIR
allure generate target/allure-results --clean -o $REPORT_DIR

# Only try to open locally (skip in CI)
if [ -z "$CI" ]; then
  echo "🌐 Opening Allure report locally..."
  allure open $REPORT_DIR
else
  echo "✅ Allure report generated for CI at $REPORT_DIR"
fi