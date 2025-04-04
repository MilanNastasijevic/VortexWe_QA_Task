package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Attachment;

public class AllureUtils {

    @Attachment(type = "image/png")
    public static byte[] attachScreenshot(Page page) {
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }
}