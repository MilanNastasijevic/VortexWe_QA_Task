package Jira;

import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.annotations.*;
import io.qameta.allure.*;
import PageObjects.KanbanBoardPage;
import testUtils.AllureMetadataWriter;
import utils.AllureUtils;
import utils.BrowserManager;

import java.lang.reflect.Method;
import java.util.List;

@Listeners({io.qameta.allure.testng.AllureTestNg.class})
@Epic("Kanban Board")
@Feature("Tile Management")
public class Board {

    KanbanBoardPage boardPage;
    BrowserManager.BrowserTypeEnum currentBrowser;

    @DataProvider(name = "browsers", parallel = true)
    public Object[][] provideBrowsers() {
        return new Object[][]{
                {BrowserManager.BrowserTypeEnum.CHROMIUM},
                {BrowserManager.BrowserTypeEnum.FIREFOX}
        };
    }

    @BeforeMethod(alwaysRun = true)
    public void setupAllureResultDirectory(Method method, Object[] testData) {
        if (testData.length > 0 && testData[0] instanceof BrowserManager.BrowserTypeEnum) {
            currentBrowser = (BrowserManager.BrowserTypeEnum) testData[0];

            String resultDir = "target/allure-results-" + currentBrowser.name().toLowerCase() + "-" + Thread.currentThread().getId();
            System.setProperty("allure.results.directory", resultDir);

            System.out.println("Allure result directory set: " + resultDir);
        }
    }

    @Test(dataProvider = "browsers")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Change tile status on multiple browsers")
    public void changeTileStatusTest(BrowserManager.BrowserTypeEnum browserType) throws InterruptedException {
        // ✅ Set result dir early for Allure
        String resultDir = "target/allure-results-" + browserType.name().toLowerCase() + "-" + Thread.currentThread().getId();
        System.setProperty("allure.results.directory", resultDir);

        runOnBrowser(browserType, page -> {
            Allure.parameter("browser", browserType.name());
            Allure.getLifecycle().updateTestCase(tc ->
                    tc.setName("changeTileStatusTest [" + browserType.name() + "]")
            );
            boardPage = new KanbanBoardPage(page);
            boardPage.changeTileStatus("Angular Spotify", "Backlog", "Selected for Development");
        });
    }

    @Test(dataProvider = "browsers")
    @Severity(SeverityLevel.NORMAL)
    @Description("Update issue counter across browsers")
    @Parameters({"browser"})
    public void updateIssueCounterTest(BrowserManager.BrowserTypeEnum browserType) throws InterruptedException {

    }



    private void runOnBrowser(BrowserManager.BrowserTypeEnum browserType, BrowserTestLogic logic) throws InterruptedException {
        try {
            BrowserManager.launchBrowser(browserType, true);
            BrowserManager.createContextAndPage();
            Page page = BrowserManager.getPage();
            page.navigate("https://jira.trungk18.com/project/board");

            logic.run(page);
        } finally {
            BrowserManager.closeContext();
            BrowserManager.closeBrowser();
        }
    }

    @FunctionalInterface
    interface BrowserTestLogic {
        void run(Page page) throws InterruptedException;
    }

    @AfterSuite(alwaysRun = true)
    public void writeAllureMetadata() {
       AllureMetadataWriter.writeAllureMetadata();
    }
}