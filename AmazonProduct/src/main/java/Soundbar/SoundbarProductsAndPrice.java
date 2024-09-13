package Soundbar;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SoundbarProductsAndPrice {
	WebDriver driver;
	
    @BeforeClass
    public void setUp() {
        // setting up the WebDriverManager to launch chromebrowser then maximising the window and keeping implicit wait 
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    
    //TestNG test Method

    @Test
    public void SoundbarPrices() {
        driver.get("https://www.amazon.in");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        // Searching for LG soundbar by passing text into search bar and submitting it
        WebElement searchBox = driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
        searchBox.sendKeys("lg soundbar");
        searchBox.submit();
       
      // Get product names and prices and storing them in a List of type WebElement
       List<WebElement> productNames = driver.findElements(By.xpath("//span[@class='a-size-medium a-color-base a-text-normal']"));     
       List<WebElement> productPrices = driver.findElements(By.xpath("//span[@class='a-price-whole']"));
       //Creating object of HashMap of type String, Integer for storing Soundbar names and Price of them
        Map<String, Integer> productPriceMap = new HashMap<>();
       //iterating over product names and getting price of each item, if price is not present then we are assigning 0 against it by using terenary operator
        for (int i = 0; i < productNames.size(); i++) {
            String productName = productNames.get(i).getText();
            String priceText = (i < productPrices.size()) ? productPrices.get(i).getText().replace(",", "") : "0";
       //since price is in text, we are convering it to integer by using parseInt method
            int price = 0;
            try {
             price =Integer.parseInt(priceText);
            }
            catch(Exception e) {
            	System.out.println("Invalid price format for product: " + productName + ", setting price to 0");
            }
            productPriceMap.put(productName, price);
        }

        // Sorting products by price and converting map to list of values 
        List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productPriceMap.entrySet());
        sortedProducts.sort(Comparator.comparingInt(Map.Entry::getValue));

        // Print sorted products by iterating over the list
        for (Map.Entry<String, Integer> entry : sortedProducts) {
            System.out.println(entry.getValue() + " " + entry.getKey());
        }
    }
   //A base method for tearing down the driver after the execution of test cases
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
