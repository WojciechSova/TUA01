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
    private final By detailsBtn = By.id("details");
    private final By tableData = By.tagName("td");

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
        driver.findElement(detailsBtn).click();
        return new ProfileDetailsPage(driver);
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
}
