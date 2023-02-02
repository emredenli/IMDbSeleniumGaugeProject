package step;

import com.thoughtworks.gauge.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import driver.Driver;
import methods.Methods;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class StepImplementation extends Driver {

    Methods methods;
    Logger logger = LogManager.getLogger(Methods.class);

    List<String> directors;
    List<String> writers;
    List<String> stars;

    public StepImplementation (){
        this.methods = new Methods();
    }

    @Step("<seconds> saniye bekle")
    public void waitElement(long seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
        System.out.println(seconds + " saniye beklendi");
        logger.info(seconds + " saniye beklendi");
    }

    @Step("<key> - <keyType> elementine tıklanır")
    public void clickElement(String key, String keyType) {
        WebElement element = methods.getElementByKey(key, keyType);

        if (element == null) {
            System.out.println("!!! HATA ( Element == Null ) HATA !!!");
            logger.error("!!! HATA ( Element == Null ) HATA !!!");
            webDriver.quit();
        }

        if (methods.isDisplayedAndEnabled(element)) {
            methods.clickElement(element);
            System.out.println("( " + key + " ) elementine tiklandi");
            logger.info("( " + key + " ) elementine tiklandi");
        } else {
            System.out.println("( " + key + " ) elementine tiklanamadi");
            logger.info("( " + key + " ) elementine tiklanamadi");
        }

    }

    @Step("<key> - <keyType> elementine <text> değerini yaz")
    public void sendKeysElement(String key, String keyType, String text) {

        WebElement element = methods.getElementByKey(key, keyType);
        text = text.endsWith("KeyValue") ? Driver.TestMap.get(text).toString() : text;

        if (element.isDisplayed() && element.isEnabled()) {
            methods.clickElement(element);
            methods.sendKeys(element, text);
            System.out.println("( " + key + " ) elementine ( " + text + " ) degeri yazildi");
            logger.info("( " + key + " ) elementine ( " + text + " ) degeri yazildi");
        } else {
            System.out.println("( " + key + " ) elementi sayfada goruntulenemedi.");
            logger.info("( " + key + " ) elementi sayfada goruntulenemedi.");
            webDriver.quit();
        }
    }

    @Step("<key> - <keyType> elementinin sayfada görünür olmadığı kontrol edilir")
    public void checkElementVisible(String key, String keyType){
        List<WebElement> elements = methods.getElements(key, keyType);
        int elementSize = elements.size();

        if(elementSize > 0) {
            System.out.println("( " + key + ") elementi gorunur ");
            logger.info("( " + key + ") elementi gorunur ");
            webDriver.quit();
        } else {
            System.out.println("( " + key + ") elementinin sayfada gorunur olmadigi onaylandi ");
            logger.info("( " + key + ") elementinin sayfada gorunur olmadigi onaylandi ");
        }

    }

    @Step("<key> - <keyType> elementinin görünür olması kontrol edilir")
    public void checkVisibleElement(String key, String keyType) {

        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        WebElement element = methods.getElementByKey(key, keyType);
        By locator = null;

        switch (keyType) {

            case "id":
                locator = By.id(key);
                break;

            case "cssSelector":
                locator = By.cssSelector(key);
                break;

            case "xpath":
                locator = By.xpath(key);
                break;

            case "className":
                locator = By.className(key);
                break;

            case "tagName":
                locator = By.tagName(key);
                break;

            case "name":
                locator = By.name(key);
                break;

            default:
                System.out.println("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                logger.info("( " + key + " ) elementi icin -> Hatali 'keyType'(" + keyType + ") gonderildi!!!");
                break;
        }

        if (locator == null) {
            System.out.println("!!! HATA ( Locator == Null ) HATA !!!");
            logger.error("!!! HATA ( Locator == Null ) HATA !!!");
            webDriver.quit();
        }

        element = wait.until(visibilityOfElementLocated(locator));

        if (element != null && element.isDisplayed()){
            System.out.println("( " + key + " ) elementi sayfada goruntulendi");
            logger.info("( " + key + " ) elementi sayfada goruntulendi");
        } else {
            System.out.println("( " + key + " ) elementi sayfada goruntulenemedi");
            logger.info("( " + key + " ) elementi sayfada goruntulenemedi");
        }

    }

    @Step("<key> - <keyType> elementine scroll yapılır")
    public void scrollElement(String key, String keyType) throws InterruptedException{

        WebElement element = methods.getElementByKey(key, keyType);

        if (methods.isDisplayedAndEnabled(element)) {
            System.out.println("( " + key + " elementine scroll yapildi ");
            logger.info("( " + key + " elementine scroll yapildi ");
            ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500);
        } else {
            System.out.println("( " + key + " ) elementine scroll yapilamadi");
            logger.info("( " + key + " ) elementine scroll yapilamadi");
        }

    }

    @Step("<url> gelen url ile aynı mı")
    public void urlControl(String url){

        methods.getUrl(url);

    }

    @Step("Director değerlerini diziye yaz")
    public List<String> writeDirectorList(){

        String key = "(//button[text()='Director']//..//ul)[1]//li";
        List<String> directorList = new ArrayList<String>();
        List<WebElement> items = webDriver.findElements(By.xpath(key));
        int elementSize = items.size();

        for( int i = 0 ; i < elementSize ; i++ ){
            String itemLocator = key + "[" + ( i + 1 ) + "]";
            String itm = webDriver.findElement(By.xpath(itemLocator)).getText();
            directorList.add(itm);
        }

        System.out.println("directorList : " + directorList);
        directors = directorList;
        return directorList;

    }

    @Step("Director değerlerini karşılaştır")
    public void getListandDirectorCompare(){

        String key = "(//button[text()='Director']//..//ul)[1]//li";
        List<String> directorList = new ArrayList<String>();
        List<WebElement> items = webDriver.findElements(By.xpath(key));
        int elementSize = items.size();

        for( int i = 0 ; i < elementSize ; i++ ){
            String itemLocator = key + "[" + ( i + 1 ) + "]";
            String itm = webDriver.findElement(By.xpath(itemLocator)).getText();
            directorList.add(itm);
        }

        if( directorList.equals(this.directors)){
            System.out.println("Director sonucları birbirine esit");
            items.clear();
        } else {
            System.out.println("Director sonuclari birbirine esit degil!");
            items.clear();
            webDriver.quit();
        }

    }

    @Step("Writer değerlerini diziye yaz")
    public List<String> writeWriterList(){

        String key = "(//button[text()='Writers']//..//ul)[1]//li";
        List<String> writerList = new ArrayList<String>();
        List<WebElement> items = webDriver.findElements(By.xpath(key));
        int elementSize = items.size();

        for( int i = 0 ; i < elementSize ; i++ ){
            String itemLocator = key + "[" + ( i + 1 ) + "]";
            String itm = webDriver.findElement(By.xpath(itemLocator)).getText();
            writerList.add(itm);
        }

        System.out.println("writerList : " + writerList);
        writers = writerList;
        return writerList;

    }

    @Step("Writer değerlerini karşılaştır")
    public void getListandWriterCompare(){

        String key = "(//button[text()='Writers']//..//ul)[1]//li";
        List<String> writerList = new ArrayList<String>();
        List<WebElement> items = webDriver.findElements(By.xpath(key));
        int elementSize = items.size();

        for( int i = 0 ; i < elementSize ; i++ ){
            String itemLocator = key + "[" + ( i + 1 ) + "]";
            String itm = webDriver.findElement(By.xpath(itemLocator)).getText();
            writerList.add(itm);
        }

        if( writerList.equals(this.writers)){
            System.out.println("Writers sonucları birbirine esit");
            items.clear();
        } else {
            System.out.println("Writers sonuclari birbirine esit degil!");
            items.clear();
            webDriver.quit();
        }

    }

    @Step("Star değerlerini diziye yaz")
    public List<String> writeStarList(){

        String key = "(//a[text()='Stars']//..//ul)[1]//li";
        List<String> starList = new ArrayList<String>();
        List<WebElement> items = webDriver.findElements(By.xpath(key));
        int elementSize = items.size();

        for( int i = 0 ; i < elementSize ; i++ ){
            String itemLocator = key + "[" + ( i + 1 ) + "]";
            String itm = webDriver.findElement(By.xpath(itemLocator)).getText();
            starList.add(itm);
        }

        System.out.println("writerList : " + starList);
        stars = starList;
        return starList;

    }

    @Step("Star değerlerini karşılaştır")
    public void getListandStarCompare(){

        String key = "(//a[text()='Stars']//..//ul)[1]//li";
        List<String> starList = new ArrayList<String>();
        List<WebElement> items = webDriver.findElements(By.xpath(key));
        int elementSize = items.size();

        for( int i = 0 ; i < elementSize ; i++ ){
            String itemLocator = key + "[" + ( i + 1 ) + "]";
            String itm = webDriver.findElement(By.xpath(itemLocator)).getText();
            starList.add(itm);
        }

        if( starList.equals(this.stars)){
            System.out.println("Stars sonucları birbirine esit");
            items.clear();
        } else {
            System.out.println("Stars sonuclari birbirine esit degil!");
            items.clear();
            webDriver.quit();
        }

    }

    @Step("Broken image link control")
    public void brokenImageLinkControl(){

        HttpURLConnection huc = null;
        int respCode = 200;

        List<WebElement> images = webDriver.findElements(By.cssSelector("#media_index_thumbnail_grid a img"));
        System.out.println(images);

        Iterator<WebElement> it = images.iterator();

        while(it.hasNext()){

            String url = it.next().getAttribute("src");
            System.out.println(url);

            if(url == null || url.isEmpty()){
                System.out.println("URL is either not configured for anchor tag or it is empty");
                continue;
            }

            try {
                huc = (HttpURLConnection)(new URL(url).openConnection());
                huc.setRequestMethod("HEAD");
                huc.connect();
                respCode = huc.getResponseCode();
                if(respCode >= 400){
                    System.out.println(url+" is a broken link");
                }
                else{
                    System.out.println(url+" is a valid link");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
