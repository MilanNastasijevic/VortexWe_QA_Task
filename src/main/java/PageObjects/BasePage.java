package PageObjects;

import com.microsoft.playwright.*;

public abstract class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected Locator getElementByText(String tag, String text) {
        return page.locator(String.format("//%s[contains(text(), '%s')]", tag, text));
    }

    protected void waitForClick(Locator locator) {
        locator.waitFor();
        locator.click();
    }

    protected void waitAndType(Locator locator, String input) {
        locator.waitFor();
        locator.clear();
        locator.pressSequentially(input);
    }

    protected void wait(int millis) {
        page.waitForTimeout(millis);
    }

    protected Locator buildLocator(String xpath) {
        return page.locator(xpath);
    }

    protected String getTextContent(Locator locator) {
        locator.waitFor();
        return locator.textContent();
    }

    protected Integer count(String countState){
        Locator countLoc = buildLocator(String.format(
                "//div[contains(@class, 'status-list') and div[contains(normalize-space(),'%s')]]", countState));
        return Integer.parseInt(getTextContent(countLoc.locator("span.lowercase")));
    }
}
