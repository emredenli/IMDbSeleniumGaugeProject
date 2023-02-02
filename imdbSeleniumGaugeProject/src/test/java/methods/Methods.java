package methods;

import driver.Driver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class Methods {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    WebDriver webDriver;
    FluentWait<WebDriver> wait;
    JavascriptExecutor jsdriver;
    long waitElementTimeout;
    long pollingEveryValue;

    public Methods(){

        this.webDriver = Driver.webDriver;
        wait = new FluentWait<>(webDriver);
        wait = setFluentWait(waitElementTimeout);
        jsdriver = (JavascriptExecutor) webDriver;
    }

    public void navigateToBack(){

        webDriver.navigate().back();
    }

    public void navigateToForward(){

        webDriver.navigate().forward();
    }

    public void clickElement(WebElement webElement){

        Actions actions = new Actions(webDriver);
        actions.click(webElement).build().perform();
    }

    public boolean isDisplayedAndEnabled(WebElement webElement){

        if (webElement.isDisplayed() && webElement.isEnabled()) {
            return true;
        } else {
            return false;
        }

    }
    public FluentWait<WebDriver> setFluentWait(long timeout){

        FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(webDriver);
        fluentWait.withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(pollingEveryValue))
                .ignoring(NoSuchElementException.class);
        return fluentWait;
    }

    public void waitByMilliSeconds(long milliSeconds){

        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitBySeconds(long seconds){

        waitByMilliSeconds(seconds*1000);
    }

    public void hoverElement(WebElement webElement) {

        Actions hoverAction = new Actions(webDriver);
        hoverAction.moveToElement(webElement).build().perform();
    }

    public void sendKeys(WebElement webElement, String text) {

        Actions actions = new Actions(webDriver);
        actions.sendKeys(webElement, text).build().perform();
    }

    public WebElement getElementByKey(String key, String keyType){
        WebElement element = null;
        switch (keyType){

            case "id":
                element = webDriver.findElement(By.id(key));
                break;

            case "cssSelector":
                element = webDriver.findElement(By.cssSelector(key));
                break;

            case "xpath":
                element = webDriver.findElement(By.xpath(key));
                break;

            case "className":
                element = webDriver.findElement(By.className(key));
                break;

            case "tagName":
                element =  webDriver.findElement(By.tagName(key));
                break;

            case "name":
                element = webDriver.findElement(By.name(key));
                break;

            default:
                System.out.println("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                logger.error("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                webDriver.quit();
                break;

        }
        return element;
    }

    public List<WebElement> getElements(String key, String keyType){
        List<WebElement> elements = null;
        switch (keyType){

            case "id":
                elements = webDriver.findElements(By.id(key));
                break;

            case "cssSelector":
                elements = webDriver.findElements(By.cssSelector(key));
                break;

            case "xpath":
                elements = webDriver.findElements(By.xpath(key));
                break;

            case "className":
                elements = webDriver.findElements(By.className(key));
                break;

            case "tagName":
                elements =  webDriver.findElements(By.tagName(key));
                break;

            case "name":
                elements = webDriver.findElements(By.name(key));
                break;

            default:
                System.out.println("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                logger.error("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                webDriver.quit();
                break;

        }
        return elements;
    }

    public void getUrl(String url){

        String currentUrl = webDriver.getCurrentUrl();
        System.out.println("currentUrl : " + currentUrl);

        String getUrl = url;
        System.out.println("getUrl : " + getUrl);

        if ( currentUrl.equals(getUrl) ){
            System.out.println("Url'ler birbirine esit.");
        } else {
            System.out.println("Url'ler birbirine esit degil!");
            webDriver.quit();
        }

    }

}
