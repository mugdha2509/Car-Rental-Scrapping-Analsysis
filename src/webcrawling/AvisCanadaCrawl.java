
package webcrawling;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class AvisCanadaCrawl {

    public static void initDriver() {
        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get(avisUrl);
    }
    public static String avisUrl = "https://www.avis.ca/en/home";

    public static void main(String[] args) {
    }

    public static void checkForPopUp() {
        By popupButtonLocator = By.cssSelector(".bx-row.bx-row-submit button[data-click='close']");
        try {
            WebElement popupButton = wait.until(ExpectedConditions.presenceOfElementLocated(popupButtonLocator));
            popupButton.click();
        } catch (Exception e) {
        }
    }

    static ChromeOptions chromeOptions = new ChromeOptions();
    static WebDriver driver;
    static WebDriverWait wait;



    public static String userPickupLoc = "";

    public static String resolveLocation(String pickupLocation, String picLoc_dropdown, String suggestionBox) {
        checkForPopUp();
        userPickupLoc = pickupLocation;
        WebElement inputPickUpField = driver.findElement(By.id(picLoc_dropdown));
        inputPickUpField.clear();
        inputPickUpField.sendKeys(pickupLocation);
        return fetchPickupLocations(driver, suggestionBox);
    }

    static boolean findSuggestion = false;

    public static String fetchPickupLocations(WebDriver driver, String suggestionId) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, List<String>> suggestionMap = new HashMap<>();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement suggestionDiv = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(suggestionId)));
        WebElement angucompleteResults = suggestionDiv.findElement(By.className("angucomplete-results"));
        List<WebElement> childElements = angucompleteResults.findElements(By.xpath("./div"));
        HashMap<String, WebElement> locationElementMap = new HashMap<>();

        for (WebElement childElement : childElements) {
            WebElement divWebelement = childElement.findElement(By.xpath(".//div[contains(@class, 'angucomplete-selection-category')]"));
            String pString = divWebelement.findElement(By.tagName("p")).getText();
            if (pString.contains("Airport Rental Locations") || pString.contains("Neighborhood Rental Locations")) {
                String spanString = divWebelement.findElement(By.tagName("span")).getText();
                String categoryName = pString.replace(spanString, "").trim();
                List<String> divList = new ArrayList<>();
                List<WebElement> divAllLocInCategory = childElement.findElements(By.cssSelector("div.angucomplete-description"));
                for (WebElement div : divAllLocInCategory) {
                    String locName = div.findElement(By.tagName("span")).getText();
                    if (findSuggestion) {
                        locName = div.findElements(By.tagName("span")).stream().map(WebElement::getText).collect(Collectors.joining());
                    }
                    divList.add(locName);
                    locationElementMap.put(locName, div);
                }
                if (childElements.get(childElements.size()-1) == childElement) {
                    findSuggestion = false;
                }
                suggestionMap.put(categoryName, divList);
            } else if (findSuggestion) {
                String spanString = divWebelement.findElement(By.tagName("span")).getText();
                String categoryName = pString.replace(spanString, "").trim();

                List<String> divList = new ArrayList<>();
                List<WebElement> divAllLocInCategory = childElement.findElements(By.cssSelector("div.angucomplete-description"));
                for (WebElement div : divAllLocInCategory) {
                    String locName = div.findElements(By.tagName("span")).stream().map(WebElement::getText).collect(Collectors.joining());
                    divList.add(locName);
                    locationElementMap.put(locName, div);
                }
                suggestionMap.put(categoryName, divList);
            }
        }

        if (suggestionMap.isEmpty()) {
            findSuggestion = true;
            fetchPickupLocations(driver, suggestionId);
            return "";
        }

        int index = 1;
        List<String> combinedList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : suggestionMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                System.out.println("\n" + entry.getKey());
                combinedList.addAll(entry.getValue());
                for (String element : entry.getValue()) {
                    System.out.println(index + ". " + element);
                    index++;
                }
            }
        }

        int selectedIndex;
        do {
            System.out.println("\nPlease select any one locations from the suggested locations above: ");
            Scanner locationSelection = new Scanner(System.in);
            selectedIndex = locationSelection.nextInt();
        } while (selectedIndex > combinedList.size());

        String selectedLoc = combinedList.get(selectedIndex - 1);
        locationElementMap.get(selectedLoc).click();
        if (findSuggestion) {
            fetchPickupLocations(driver, suggestionId);
        } else {
        }
        return selectedLoc;
    }

    public static void resolveDate(String pickupDate, String returnDate) {
        WebElement inputPickUpDateField = driver.findElement(By.id("from"));
        WebElement inputReturnDateField = driver.findElement(By.id("to"));
        inputPickUpDateField.clear();
        inputReturnDateField.clear();
        inputPickUpDateField.sendKeys(pickupDate);
        inputReturnDateField.sendKeys(returnDate);
    }

    private static void scrollDownToLoadAllElements(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        int currentHeight,

                newHeight = 0;

        do {
            currentHeight = newHeight;
            jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            newHeight = Integer.parseInt(jsExecutor.executeScript("return document.body.scrollHeight").toString());
        } while (newHeight > currentHeight);
    }

    public static void fetchCarDeals() {
        driver.findElement(By.id("res-home-select-car")).click();

        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (checkForWarning()) {
            return;
        }

        checkForPopUp();

        scrollDownToLoadAllElements(driver);

        String content = driver.getPageSource();

        String folderList[] = {"AvisHtml", "BudgetHtml"};
        File webPageFolder = new File("AvisHtml");
        int fileCounter = 1;
        if (webPageFolder.exists()){
            fileCounter = webPageFolder.listFiles().length;
        }

        WebCrawler.createFile(avisUrl, content, "avis_deals", "AvisFiles/");

        System.out.println("Avis deals extracted and saved in Json...");
    }

    private static boolean checkForWarning() {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("warning-msg-err")));
            if (element.isDisplayed()) {
                System.out.println(element.getText());
                driver.get(avisUrl);
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    public static void resolveTime(String pickupTime, String returnTime) {
        WebElement selectPickUpTimeElement = driver.findElement(By.xpath("//select[@ng-model='vm.reservationModel.pickUpTime']"));
        Select selectPickupTime = new Select(selectPickUpTimeElement);
        String formattedPickupTime = String.format("string:%s", pickupTime);
        selectPickupTime.selectByValue(formattedPickupTime);

        WebElement selectDropOffTimeElement = driver.findElement(By.xpath("//select[@ng-model='vm.reservationModel.dropTime']"));
        Select selectDropOffTime = new Select(selectDropOffTimeElement);
        String formattedDropOffTime = String.format("string:%s", returnTime);
        selectDropOffTime.selectByValue(formattedDropOffTime);
    }

    public static void resetDriver(){
        driver.get(avisUrl);
    }

    public static void closeDriver() {
        driver.quit();
    }
}
