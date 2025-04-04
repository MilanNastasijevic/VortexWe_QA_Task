package utils;

import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.BrowserType;


public class BrowserManager {

    private static final ThreadLocal<Playwright> playwrightThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browserThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();

    public enum BrowserTypeEnum {
        CHROMIUM, FIREFOX
    }

    public static void launchBrowser(BrowserTypeEnum browserType, boolean headless) {
        Playwright playwright = Playwright.create();
        playwrightThreadLocal.set(playwright);

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);
        Browser browser;

        switch (browserType) {
            case CHROMIUM:
                browser = playwright.chromium().launch(options);
                break;
            case FIREFOX:
                browser = playwright.firefox().launch(options);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
        }

        browserThreadLocal.set(browser);
    }

    public static void createContextAndPage() {
        Browser browser = browserThreadLocal.get();
        if (browser == null) throw new IllegalStateException("Browser not launched.");

        BrowserContext context = browser.newContext();
        contextThreadLocal.set(context);

        Page page = context.newPage();
        pageThreadLocal.set(page);
    }

    public static Page getPage() {
        Page page = pageThreadLocal.get();
        if (page == null) throw new IllegalStateException("Page is null, did you forget to call createContextAndPage()?");
        return page;
    }

    public static void closeContext() {
        BrowserContext context = contextThreadLocal.get();
        if (context != null) {
            context.close();
            contextThreadLocal.remove();
        }
    }

    public static void closeBrowser() {
        Browser browser = browserThreadLocal.get();
        if (browser != null) {
            browser.close();
            browserThreadLocal.remove();
        }

        Playwright playwright = playwrightThreadLocal.get();
        if (playwright != null) {
            playwright.close();
            playwrightThreadLocal.remove();
        }
    }
}


