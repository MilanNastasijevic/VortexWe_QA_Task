package Jira;

import com.microsoft.playwright.Page;
import org.testng.annotations.*;
import io.qameta.allure.*;
import PageObjects.KanbanBoardPage;
import testUtils.AllureMetadataWriter;
import utils.BrowserManager;

import java.lang.reflect.Method;

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
                {BrowserManager.BrowserTypeEnum.FIREFOX},
                {BrowserManager.BrowserTypeEnum.WEBKIT}
        };
    }

    @BeforeMethod(alwaysRun = true)
    public void setupAllureResultDirectory(Method method, Object[] testData) {
        if (testData.length > 0 && testData[0] instanceof BrowserManager.BrowserTypeEnum) {
            currentBrowser = (BrowserManager.BrowserTypeEnum) testData[0];

            String resultDir = "target/allure-results-" + currentBrowser.name().toLowerCase() + "-" + Thread.currentThread().getId();
            System.setProperty("allure.results.directory", resultDir);

            System.out.println("✅ Allure result directory set: " + resultDir);
        }
    }

    // ... your test methods using currentBrowser instead of passing it directly


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

//    @Test(dataProvider = "browsers")
//    @Severity(SeverityLevel.NORMAL)
//    @Description("Update issue counter across browsers")
//    @Parameters({"browser"})
//    public void updateIssueCounterTest(BrowserManager.BrowserTypeEnum browserType) throws InterruptedException {
//        runOnBrowser(browserType, page -> {
//            // Attach browser context to Allure
//            Allure.parameter("browser", browserType.name());
//
//            // Update test name in Allure to include the browser
//            Allure.getLifecycle().updateTestCase(tc ->
//                    tc.setName("updateIssueCounterTest [" + browserType.name() + "]")
//            );
//
//            // Actual test logic
//            boardPage = new KanbanBoardPage(page);
//            List<Integer> s = boardPage.countValues;
//            boardPage.updateIssueCounter("Backlog", "Angular Spotify", "Selected for Development");
//            Assert.assertEquals(s.get(1), s.get(0) - 1, "Count works good");
//        });
//    }

//    @Test(dataProvider = "browsers")
//    @Severity(SeverityLevel.MINOR)
//    @Description("Edit issue modal across browsers")
//    @Parameters({"browser"})
//    public void editIssueTest(BrowserManager.BrowserTypeEnum browserType) throws InterruptedException {
//        runOnBrowser(browserType, page -> {
//            // Attach current browser name as parameter in Allure
//            Allure.parameter("browser", browserType.name());
//
//            // Update test name to be unique per browser run
//            Allure.getLifecycle().updateTestCase(tc -> {
//                tc.setName("editIssueTest [" + browserType.name() + "]");
//            });
//
//            // Execute test logic
//            boardPage = new KanbanBoardPage(page);
//            boardPage.issueModal("Angular Spotify", "New test");
//        });
//    }

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