package webcrawling;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CarRentalWebCrawl {
    public static String orbitzUrl = "https://www.carrentals.com/";

    static ChromeOptions chromeOptions = new ChromeOptions();
//    static EdgeOptions edgeOptions = new EdgeOptions();
    //        chromeOptions.addArguments("--headless");
    static WebDriver driver;
    static WebDriverWait wait;

    public static void initDriver() {

//        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);
//        driver = new EdgeDriver(edgeOptions);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.get(orbitzUrl);
        driver.navigate().refresh();
//        try {
//            while (wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("button.uitk-fake-input")))) {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.uitk-button"))).click();
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        } catch (Exception e) {
////            System.out.println(CarRentalWebCrawl.class);
//            // If the button is not present after 3 seconds, do nothing
////            System.out.println("Pop-up button not found after waiting for 1 seconds. Continuing without clicking.");
//        }
    }

    public static void main(String[] args) {
        driver.get("https://www.orbitz.com/Cars");
        driver.navigate().refresh();
        try {
            while (wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("button.uitk-fake-input")))) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.uitk-button"))).click();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (Exception e) {
//            System.out.println(CarRentalWebCrawl.class);
            // If the button is not present after 3 seconds, do nothing
//            System.out.println("Pop-up button not found after waiting for 1 seconds. Continuing without clicking.");
        }


        System.out.println("Enter your city:");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

//        fetchDeals(userInput);

        // Close the browser
//        driver.quit();
    }

    public static void fetchData(String city, String pickUpDate, String returnDate, String pickUpTime, String returnTime) {
//        driver.get("https://www.orbitz.com/Cars");
//        driver.navigate().refresh();
//        try {
//            while (wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("button.uitk-fake-input")))) {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.uitk-button"))).click();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        } catch (Exception e) {
//            // If the button is not present after 3 seconds, do nothing
//            System.out.println("Pop-up button not found after waiting for 1 seconds. Continuing without clicking.");
//        }


//        System.out.println("Enter your city:");
//        Scanner scanner = new Scanner(System.in);
//        String userInput = scanner.nextLine();

//        fetchDeals(city, pickUpDate,returnDate, pickUpTime, returnTime);
    }

    // Method to navigate to the target month based on user input date
    private static void navigateToTargetMonth(WebDriver driver, String userInputDate) {
        // Split user input date into day, month, and year
        String[] dateParts = userInputDate.split("/");
        String targetDay = dateParts[0];
        String targetMonth = dateParts[1];
        String targetYear = dateParts[2];

        // Introduce an explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        // Wait for the pagination container to be present
        WebElement paginationContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".uitk-date-picker-menu-pagination-container")));

        // Locate the buttons for previous and next month
        WebElement prevMonthButton = paginationContainer.findElement(By.xpath(".//button[contains(@aria-label, 'Previous month')]"));
        WebElement nextMonthButton = paginationContainer.findElement(By.xpath(".//button[contains(@aria-label, 'Next month')]"));

        // Get the current month and year from the page
        WebElement currentMonthElement = driver.findElement(By.cssSelector(".uitk-date-picker-month-name"));

        // Continue navigating until the target month and year are reached
        while (!currentMonthElement.getText().equals(targetMonth + " " + targetYear)) {
            // Click the appropriate button based on whether the target month is before or after the current month
            if (currentMonthElement.getText().compareTo(targetMonth + " " + targetYear) > 0) {
                prevMonthButton.click();
            } else {
                nextMonthButton.click();
            }

            // Wait for a short duration to allow the calendar to update
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Update the current month
            currentMonthElement = driver.findElement(By.cssSelector(".uitk-date-picker-month-name"));
        }
    }

    public static void resolveDate(String pickUpDate, String returnDate) {

//        System.out.println("Please enter your pick up date: ");
        String date = pickUpDate;
        String inputDate = date;

        // Split the date into day, month, and year
        String[] parts = inputDate.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

//        navigateToTargetMonth(driver, inputDate);

        // Click the button corresponding to the given date
        String buttonXPath = String.format("//button[@aria-label='%s %d, %d']", getMonthName(month), day, year);
//        System.out.println("Please enter your drop off date: ");
        String enddate = returnDate;
        String inputendDate = enddate;


        // Split the date into day, month, and year
//<button type="button" class="uitk-date-picker-day" data-day="20" aria-label="Dec 20, 2023"></button>
//        <button type="button" class="uitk-date-picker-day" data-day="22" aria-label="Dec 22, 2023"></button>
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("d1-btn"))).click();
//            System.out.println("button xpath:"+buttonXPath);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Dec 10, 2023']")));
//            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Dec 10, 2023']")));
//            button.click();
            String[] eparts = inputendDate.split("/");
            int eday = Integer.parseInt(eparts[0]);
            int emonth = Integer.parseInt(eparts[1]);
            int eyear = Integer.parseInt(eparts[2]);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(buttonXPath)));
            WebElement startDateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath)));
            startDateButton.click();
            String endDatebuttonXPath = String.format("//button[contains(@aria-label, '%s %d, %d')]", getMonthName(emonth), eday, eyear);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(endDatebuttonXPath)));
            WebElement endDateButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(endDatebuttonXPath)));
            endDateButton.click();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-stid='apply-date-picker'][aria-label='Save changes and close the date picker.']")));
            // Find the element using a CSS selector
            WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-stid='apply-date-picker'][aria-label='Save changes and close the date picker.']")));
            doneButton.click();

        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println("Element not found: " + e.getMessage());
        }
    }
    public static void resolveTime(String pickUpTime, String returnTime) {
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"Rental-cars-transportation\"]/div[2]/div[2]/div/select"))).click();
//        System.out.println("Enter Pickup Time (HH:MM-AM/PM)");
        String pickupTime = pickUpTime; // Change this variable to the time you want to select
//        System.out.println("Enter Drop off Time (HH:MM-AM/PM)");
        String dropOffTime = returnTime; // Change this variable to the time you want to select

        selectTime(pickupTime, ".uitk-field-select"); // Replace ".pickup-time-select" with the selector for pickup time
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"Rental-cars-transportation\"]/div[2]/div[3]/div/select"))).click();

        selectTime(dropOffTime, ".uitk-field-select[aria-label='Drop-off time']"); // Replace ".drop-off-time-select" with the selector for drop-off time

        WebElement searchButton = driver.findElement(By.cssSelector("button[data-testid='submit-button']"));
        searchButton.click();

        try {
            Thread.sleep(12);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
//        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-testid='car-offer-card']")));
        WebCrawler.createFile(orbitzUrl, driver.getPageSource(), "orbitz_deals", "CarRentalFiles/");
    }

    public static void handlePickUpLocation(String pickUpLoc) {
        driver.manage().window().maximize();
        wait.until(drive -> ((JavascriptExecutor) drive).executeScript("return document.readyState").equals("complete"));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"wizard-car-pwa-1\"]/div[1]/div[1]/div/div/div/div/div[2]/div[1]/button"))).click();
        WebElement inputField = driver.findElement(By.xpath("//*[@id=\"location-field-locn\"]"));
        inputField.sendKeys(pickUpLoc);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inputField.sendKeys(" ");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inputField.sendKeys(Keys.BACK_SPACE);

        inputField.sendKeys(pickUpLoc);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inputField.sendKeys(" ");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        inputField.sendKeys(Keys.BACK_SPACE);

//        inputField.sendKeys(pickUpLoc);
//        inputField.sendKeys(Keys.RETURN);

//        try {
//            Thread.sleep(2);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        inputField.sendKeys(" ");
//        driver.navigate().refresh();
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"wizard-car-pwa-1\"]/div[1]/div[1]/div/div/div/div/div[2]/div[1]/button"))).click();
//        WebElement inputField1 = driver.findElement(By.xpath("//*[@id=\"location-field-locn\"]"));
//        inputField1.sendKeys(pickUpLoc);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement locationResults = driver.findElement(By.cssSelector("ul[data-stid='location-field-locn-results']"));

        // Find all list items containing location suggestions within the parent element
        List<WebElement> locationItems = locationResults.findElements(By.cssSelector("li[data-stid='location-field-locn-result-item']"));
        int a = 1;
        // Loop through each list item and extract the location suggestion
//        System.out.println("Location Suggestions:");
        for (WebElement locationItem : locationItems) {
            // Find the button element within each list item
            WebElement buttonElement = locationItem.findElement(By.cssSelector("button[data-stid='location-field-locn-result-item-button']"));

            // Get the aria-label attribute containing the location suggestion
            String locationSuggestion = buttonElement.getAttribute("aria-label");

            // Print each location suggestion
//            System.out.println(a + ". " + locationSuggestion);
            a++;
        }
        Scanner scanner = new Scanner(System.in);
//        System.out.println("Select your location: ");
        String userInput = "1";

        List<WebElement> buttons = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("button[data-stid='location-field-locn-result-item-button']")));

        int indexToClick = Integer.parseInt(userInput); // Change the index based on your requirement (0-based index)

        // Check if the index is valid
        if (indexToClick >= 0 && indexToClick < buttons.size()) {
            // Click the button at the specified index
//            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(buttons.get(indexToClick)));
            buttons.get(indexToClick).click();

        } else {
            System.out.println("Invalid index or element not found.");
        }
    }
    public static void handleDropOffLocation(String dropOffLoc) {
        driver.manage().window().maximize();
        wait.until(drive -> ((JavascriptExecutor) drive).executeScript("return document.readyState").equals("complete"));

//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"Rental-cars-transportation\"]/div[1]/div[2]/div/div/div/div/div[2]/div[1]/button"))).click();
//        WebElement inputField = driver.findElement(By.xpath("//*[@id=\"location-field-loc2\"]"));
//        inputField.sendKeys(dropOffLoc);
//        driver.navigate().refresh();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"wizard-car-pwa-1\"]/div[1]/div[2]/div/div/div/div/div[2]/div[1]/button"))).click();
        WebElement inputField1 = driver.findElement(By.xpath("//*[@id=\"location-field-loc2\"]"));
        inputField1.sendKeys(dropOffLoc);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        WebElement locationResults = driver.findElement(By.cssSelector("ul[data-stid='location-field-loc2-results']"));

        // Find all list items containing location suggestions within the parent element
        List<WebElement> locationItems = locationResults.findElements(By.cssSelector("li[data-stid='location-field-loc2-result-item']"));
        int a = 1;
        // Loop through each list item and extract the location suggestion
//        System.out.println("Location Suggestions:");
        for (WebElement locationItem : locationItems) {
            // Find the button element within each list item
            WebElement buttonElement = locationItem.findElement(By.cssSelector("button[data-stid='location-field-loc2-result-item-button']"));

            // Get the aria-label attribute containing the location suggestion
            String locationSuggestion = buttonElement.getAttribute("aria-label");

            // Print each location suggestion
//            System.out.println(a + ". " + locationSuggestion);
            a++;
        }
        Scanner scanner = new Scanner(System.in);
//        System.out.println("Select your location: ");
        String userInput = "0";
        List<WebElement> buttons = driver.findElements(By.cssSelector("ul[data-stid='location-field-loc2-results'] button[data-stid='location-field-loc2-result-item-button']"));

        int indexToClick = Integer.parseInt(userInput); // Change the index based on your requirement (0-based index)

        // Check if the index is valid
        if (indexToClick >= 0 && indexToClick < buttons.size()) {
            // Click the button at the specified index
            buttons.get(indexToClick).click();

        } else {
            System.out.println("Invalid index or element not found.");
        }
    }

//    private static Hashtable<String, String> fetchDeals(String inputQuery, String pickUpDate, String returnDate, String pickUpTime, String returnTime) {
//
//    }

    public static void selectTime(String inputTime, String selectElementSelector) {
        // Extract hour and minute from the input time
        String[] splitTime = inputTime.split(":");
        int hour = Integer.parseInt(splitTime[0]);
        int minute = Integer.parseInt(splitTime[1].substring(0, 2));
        boolean isPM = splitTime[1].substring(2).equalsIgnoreCase("PM");

        int timeInMinutes = hour * 60 + minute;

        // Find the select element for time
        WebElement timeSelect = driver.findElement(By.cssSelector(selectElementSelector));

        // Loop through options and select the closest time
        for (WebElement option : timeSelect.findElements(By.tagName("option"))) {
            int optionTime = Integer.parseInt(option.getAttribute("data-time"));
            if (optionTime >= timeInMinutes) {
                option.click();
                break;
            }
        }
    }

    private static String getMonthName(int monthNumber) {
        String[] months = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        return months[monthNumber - 1];
    }

    public static void resetDriver(){
        driver.get(orbitzUrl);
    }

    public static void closeDriver(){
        driver.quit();
    }
}