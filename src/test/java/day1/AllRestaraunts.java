package day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
//test

public class AllRestaraunts {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Explicit wait
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int scrollCount = 0;
        Actions actions = new Actions(driver);

        driver.get("https://www.zomato.com/hyderabad/restaurants");
        driver.manage().window().maximize();

        Set<String> restaurantLinks = new HashSet<String>(); // Store unique restaurant links
        int prevSize = 0;

        while (true) {
        	 while (scrollCount < 10) {  // Limit scrolling to 10 times
                 actions.scrollByAmount(0, 2000).perform();

            // Show alert in the browser
            js.executeScript("alert('Total restaurants loaded so far: " + restaurantLinks.size() + "');");

            try {
                Thread.sleep(3000); // Wait for new restaurants to load
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<WebElement> headerLinks = driver.findElements(By.cssSelector("a.sc-hPeUyl"));
            for (WebElement link : headerLinks) {
                String href = link.getAttribute("href");
                if (href != null && !href.isEmpty()) {
                    restaurantLinks.add(href);
                }
            }

            if (restaurantLinks.size() == prevSize) {
                break;
            }
            prevSize = restaurantLinks.size();
            System.out.println("Total restaurants loaded: " + prevSize);
        }


        System.out.println("Final total restaurants: " + restaurantLinks.size());


        driver.quit();  // Ensure WebDriver closes properly

    }
    }
}