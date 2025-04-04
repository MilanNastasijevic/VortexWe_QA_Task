#!/bin/bash

echo "🧹 Preparing merged Allure results folder..."
mkdir -p target/allure-results

echo "📦 Merging browser-specific results..."
find target -type d -name "allure-results-*" | while read dir; do
  echo "→ Merging: $dir"
  cp -r "$dir"/* target/allure-results/ 2>/dev/null
done

echo "🧠 Re-writing metadata"
# Call your Java utility that writes to target/allure-results
mvn exec:java -Dexec.mainClass="utils.AllureMetadataWriter"

echo "📊 Generating Allure report"
allure generate target/allure-results --clean -o target/allure-report

echo "🌐 Opening Allure report..."
allure open target/allure-report

