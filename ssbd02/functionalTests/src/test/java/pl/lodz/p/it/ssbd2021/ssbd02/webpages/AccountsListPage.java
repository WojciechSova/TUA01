package pl.lodz.p.it.ssbd2021.ssbd02.webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountsListPage {

    protected WebDriver driver;
    private final By usersTable = By.id("users");
    private final By tableHeader = By.tagName("th");
    private final By detailsButton = By.id("details");
    private final By tableData = By.tagName("td");
    private final By tableRow = By.tagName("tr");
    private final By tableDataLogin = By.xpath("./td[1]");
    private final By blockUnblockButton = By.xpath("./td[5]/button");
    private final By tableDataAccessLevels = By.xpath("./td[4]");
    private final By tableDataAccessLevelsButton = By.xpath("./td[6]/button");

    public AccountsListPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isTableDisplayed() {
        return driver.findElement(usersTable).isDisplayed();
    }

    public List<String> getTableHeaders() {
        return driver.findElements(tableHeader).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public ProfileDetailsPage openAnotherUserProfileDetails() {
        driver.switchTo().defaultContent();
        driver.findElement(detailsButton).click();
        return new ProfileDetailsPage(driver);
    }

    public ChangeAccessLevelsPage openChangeAccessLevelsForm(String login) {
        getUserWithLogin(login).findElement(tableDataAccessLevelsButton).click();
        return new ChangeAccessLevelsPage(driver);
    }

    public List<String> getTableData() {
        List<String> allTds = driver.findElements(tableData).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        return IntStream.range(0, allTds.size())
                .filter(i -> i % getNumberOfColumns() <= 3)
                .mapToObj(allTds::get)
                .map(String::toString)
                .collect(Collectors.toList());
    }

    private int getNumberOfColumns() {
        return getTableHeaders().size();
    }

    public By getUsersTable() {
        return usersTable;
    }

    public List<WebElement> getTableRows() {
        List<WebElement> elements = driver.findElements(tableRow);
        elements.remove(0);
        return elements;
    }

    public String getActiveUserLogin() {
        WebElement element = getTableRows()
                .stream()
                .filter(row -> row.findElement(blockUnblockButton).getText().equals("Zablokuj"))
                .findFirst()
                .get();

        return element.findElement(tableDataLogin).getText();
    }

    public WebElement getUserWithLogin(String login) {
        return getTableRows()
                .stream()
                .filter(row -> row.findElement(tableDataLogin).getText().equals(login))
                .findFirst()
                .get();
    }

    public void blockUser(String login) {
        getUserWithLogin(login).findElement(blockUnblockButton).click();
    }

    public By getBlockUnblockButton() {
        return blockUnblockButton;
    }

    public boolean isUserActive(String login) {
        return getUserWithLogin(login)
                .findElement(blockUnblockButton).getText().equals("Zablokuj");
    }

    public String getAccessLevels(String login) {
        return getUserWithLogin(login).findElement(tableDataAccessLevels).getText();
    }
}
