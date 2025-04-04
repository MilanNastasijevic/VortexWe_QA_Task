package PageObjects;

import com.microsoft.playwright.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KanbanBoardPage extends BasePage {

    public List<Integer> countValues = new ArrayList<>();

    public String xPathSelectedForDevelopment = "//div/span[contains(normalize-space(),'Selected for Development')]";

    public KanbanBoardPage(Page page) {
        super(page);
    }

    public void changeTileStatus(String tileText, String currentStatusText, String newStatusText) {
        waitForClick(getElementByText("p", tileText));

        Locator statusButton = buildLocator(String.format(
                "//button[contains(@class, 'btn-secondary') and span[contains(normalize-space(), '%s')]]", currentStatusText));
        waitForClick(statusButton);

        List<String> first = statusListState();


        wait(2000);

        Locator statusOption = buildLocator(String.format("//span[contains(normalize-space(),'%s')]", newStatusText));
        waitForClick(statusOption);

        Locator statusButton1 = buildLocator(String.format(
                "//button[contains(@class, 'btn-secondary') and span[contains(normalize-space(), '%s')]]", newStatusText));
        waitForClick(statusButton1);

        wait(2000);

        List<String> second = statusListState();

        updatedList(first,second);

        wait(2000);
    }

    public List<String> statusListState(){

        Locator dropdownOptions = page.locator("//ul/li/div/span");
        List<String> optionTexts = dropdownOptions.allInnerTexts();
        optionTexts.forEach(System.out::println);

        return optionTexts;

    }

    public List<String> updatedList(List<String> optionTexts, List<String> updatedOptions){

        // One old option must have been removed (calculate removed items)
        List<String> removedOptions = optionTexts.stream()
                .filter(opt -> !updatedOptions.contains(opt))
                .collect(Collectors.toList());

        return removedOptions;
    }

    public void updateIssueCounter(String sourceStatus, String tile, String targetStatus) {

        Integer ccc = count(sourceStatus);
        changeTileStatus(tile, sourceStatus, targetStatus);
        Integer vvv = count(sourceStatus);

        countValues.add(ccc);
        countValues.add(vvv);
    }

    public void issueModal(String tileText, String textInput) {
        waitForClick(getElementByText("p", tileText));

        Locator modalTitle = buildLocator("//issue-title/textarea");
        waitAndType(modalTitle, textInput);

        wait(2000);
    }


}
//public class KanbanBoardPage {
//
//    private final Page page;
//
//    public List<Integer> countValues = new ArrayList<>();
//
//    public KanbanBoardPage(Page page) {
//        this.page = page;
//    }
//
//
//    public void changeTileStatus(String tileText, String currentStatusText, String newStatusText) throws InterruptedException {
//        Locator tile = page.locator(String.format("//p[contains(text(), '%s')]", tileText));
//        tile.waitFor();
//        tile.click();
//        Thread.sleep(5000);
//
//        Locator statusButton = page.locator(String.format("//button[contains(@class, 'btn-secondary') and span[contains(normalize-space(), '%s')]]", currentStatusText));
//        statusButton.waitFor();
//        statusButton.click();
//
//        Locator statusOption = page.locator(String.format("//span[contains(normalize-space(),'%s')]", newStatusText));
//        statusOption.waitFor();
//        statusOption.click();
//
//        page.waitForTimeout(5000); // Adjust or replace with proper wait
//    }
//
//    public List<Integer> updateIssueCounter(String sourceStatus, String tile, String targetStatus) throws InterruptedException {
//
//
//        Locator countIssue = page.locator(String.format("//div[contains(@class, 'status-list') and div[contains(normalize-space(),'%s')]]", sourceStatus));
//        Integer initialCount = Integer.parseInt(countIssue.locator("span.lowercase").textContent());
//
//        changeTileStatus(tile, sourceStatus, targetStatus);
//
//        Locator UpdatecountIssue = page.locator(String.format("//div[contains(@class, 'status-list') and div[contains(normalize-space(),'%s')]]", targetStatus));
//        Integer updateCount = Integer.parseInt(countIssue.locator("span.lowercase").textContent());
//
//
//        countValues.add(initialCount);
//        countValues.add(updateCount);
//        System.out.println(countValues);
//        return countValues;
//    }
//
//    public void issueModal(String tileText, String textInput) throws InterruptedException {
//
//        Locator tile = page.locator(String.format("//p[contains(text(), '%s')]", tileText));
//        tile.waitFor();
//        tile.click();
//        Thread.sleep(2000);
//
//        Locator modalTitle = page.locator("//issue-title/textarea");
//        modalTitle.waitFor();
//        modalTitle.clear();
//        modalTitle.pressSequentially(textInput);
//        Thread.sleep(2000);
//    }
//
//}

//    public void singleAssignee(){
//
//    }
//
//    public void switchingIssueType(){}
//
//}
