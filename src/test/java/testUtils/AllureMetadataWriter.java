package testUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AllureMetadataWriter {

    private static final Path RESULTS_DIR = Paths.get("target/allure-results");

    public static void writeAllureMetadata() {
        try {
            Files.createDirectories(RESULTS_DIR);
            writeEnvironmentProperties();
            writeExecutorJson();
            System.out.println("✅ Allure metadata written.");
        } catch (Exception e) {
            System.out.println(" Failed to write Allure metadata: " + e.getMessage());
        }
    }

    private static void writeEnvironmentProperties() throws IOException {
        Properties props = new Properties();
        props.setProperty("Browser", "Chromium, Firefox, WebKit");
        props.setProperty("Environment", getEnv("ENV", "Local"));
        props.setProperty("BaseURL", "https://jira.trungk18.com");

        try (FileWriter writer = new FileWriter(RESULTS_DIR.resolve("environment.properties").toFile())) {
            props.store(writer, "Allure Environment");
        }
    }

    private static void writeExecutorJson() throws IOException {
        Map<String, String> executor = new HashMap<>();

        String ciType = detectCI();
        executor.put("name", ciType);
        executor.put("type", "CI");
        executor.put("buildName", getEnv("CI_JOB_NAME", "Local Run"));
        executor.put("buildUrl", getEnv("CI_JOB_URL", getEnv("BUILD_URL", "http://localhost:8080/job/local-run")));
        executor.put("reportUrl", getEnv("ALLURE_REPORT_URL", "http://localhost:8080/job/local-run/allure"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(RESULTS_DIR.resolve("executor.json").toFile())) {
            gson.toJson(executor, writer);
        }
    }

    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }

    private static String detectCI() {
        if (System.getenv("GITLAB_CI") != null) return "GitLab CI";
        if (System.getenv("JENKINS_HOME") != null) return "Jenkins";
        return "Local";
    }
}
