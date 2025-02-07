package day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FirstTest {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Explicit wait
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.zomato.com/hyderabad/restaurants");
        driver.manage().window().maximize();

        Set<String> restaurantLinks = new HashSet<String>(); // Store unique restaurant links
        int prevSize = 0;

        // **Scroll down dynamically until all restaurants are loaded**
        while (true) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try {
                Thread.sleep(3000); // Wait for new restaurants to load
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<WebElement> headerLinks = driver.findElements(By.cssSelector("a.sc-hPeUyl"));

            for (WebElement link : headerLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    restaurantLinks.add(href); // Add only unique links
                }
            }

            if (restaurantLinks.size() == prevSize) {
                break; // Stop scrolling when no new restaurants load
            }
            prevSize = restaurantLinks.size();
            System.out.println("Total restaurants loaded: " + prevSize);
        }

        System.out.println("Final total restaurants: " + restaurantLinks.size());

        // **Open each restaurant link in a new tab and extract details**
        for (String href : restaurantLinks) {
            js.executeScript("window.open(arguments[0]);", href); // Open link in new tab

            // Switch to new tab
            List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabs.size() - 1));

            // Extract restaurant details
            try {
                WebElement restaurantName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sc-7kepeu-0")));
                WebElement reviews = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sc-1q7bklc-8")));
                WebElement rating = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sc-1q7bklc-1")));
                System.out.print(restaurantName.getText()+" "+rating.getText()+" "+reviews.getText());
        
            } catch (Exception e) {
                System.out.println("Unable to fetch restaurant details.");
            }

            // Close current tab and switch back to the main tab
            driver.close();
            driver.switchTo().window(tabs.get(0));
        }

        driver.quit(); // Close the browser
    }
}
