package utils;

import io.qameta.allure.Allure;

public class StepHelper {
    public static void doStep(String name, Runnable action) {
        Allure.step(name, () -> {
            action.run();
        });
    }
}
