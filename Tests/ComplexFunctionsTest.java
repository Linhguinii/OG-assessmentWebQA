
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.jetty9.util.log.Log;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

// TODO: Don't move the mouse when running test. It may conflict with some tests. As well as close any Spotify application if open.
// @pre: There is at least a few songs in queue to allow subsequent operations && start on Pause(or Play in HTML)
public class ComplexFunctionsTest {
    WebDriver webDriver;
    WebDriverWait wait;
    String url;
    SpotifyMainPage mainPage;
    SpotifyStartPage startPage;
    @BeforeTest
    public void setup(){
        // TODO: local chrome driver
        System.setProperty("webdriver.chrome.driver",);
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, 5);
        url = "https://open.spotify.com/";
        webDriver.get(url);
        startPage = new SpotifyStartPage(webDriver);
        clearCookie();
        LogInTest();
        mainPage = new SpotifyMainPage(webDriver);
        // @pre : start on Play(HTML)
        try{
            wait.until(ExpectedConditions.attributeContains(mainPage.getByPlay(),"aria-label","Play"));
        }
        catch(Exception e){
            mainPage.togglePlayButton();
            wait.until(ExpectedConditions.attributeContains(mainPage.getByPlay(),"aria-label","Play"));
        }
    }

    public void LogInTest(){
        startPage.clickSpotifyLoginPage();
        SpotifyLoginPage loginPage = new SpotifyLoginPage(webDriver);
        By byLogin = loginPage.getByLogin();

        // Set up explicit wait
        // Wait for the loading of the MainPage
        wait.until(ExpectedConditions.visibilityOfElementLocated(byLogin));
        // TODO: Spotify credentials
        loginPage.loginToSpotify();
    }

    public void clearCookie(){
        By cookieButton = startPage.getByCookieButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cookieButton));
        startPage.clickCookieExitButton();
    }

    @Test
    // Sequence : Repeat, Repeat, Play
    public void ComplexFunctionTest1(){
        // Set up
        By byRepeat = mainPage.getByRepeat();
        // Wait for the loading of the MainPage
        wait.until(ExpectedConditions.visibilityOfElementLocated(byRepeat));

        WebElement wRepeat = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[5]"));
        WebElement playElement = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[3]"));
        if(wRepeat.getAttribute("title").equals("Enable repeat")){
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat,"title", "Enable repeat one"));
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat,"title", "Disable repeat"));
            mainPage.togglePlayButton();
            wait.until(ExpectedConditions.attributeContains(playElement,"aria-label", "Pause") );
        }
        else if(wRepeat.getAttribute("title").equals("Enable repeat one")){
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat, "title", "Disable repeat"));
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat,"title","Enable repeat"));
            mainPage.togglePlayButton();
            wait.until(ExpectedConditions.attributeContains(playElement, "aria-label", "Pause"));
        }
        else{
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat, "title", "Enable repeat"));
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat, "title", "Enable repeat one"));
            mainPage.togglePlayButton();
            wait.until(ExpectedConditions.attributeContains(playElement, "aria-label", "Pause"));
        }
    }

    @Test
    // Sequence: Shuffle, Repeat, Play
    public void ComplexFunctionTest2(){
        // Set up
        By byShuffle = mainPage.getByShuffle();
        // Wait for the loading of the MainPage
        wait.until(ExpectedConditions.visibilityOfElementLocated(byShuffle));

        // Shuffle
        WebElement wShuffle = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[1]"));
        String sShuffle = wShuffle.getAttribute("title");
        mainPage.toggleShuffleButton();
        if(sShuffle.equals("Disable shuffle")) {
            wait.until(ExpectedConditions.attributeContains(wShuffle, "title", "Enable shuffle"));
        }
        else{
            wait.until(ExpectedConditions.attributeContains(wShuffle, "title", "Disable shuffle"));
        }

        // Repeat
        WebElement wRepeat = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[5]"));
        if(wRepeat.getAttribute("title").equals("Enable repeat")){
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat, "title", "Enable repeat one"));
        }
        else if(wRepeat.getAttribute("title").equals("Enable repeat one")){
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat, "title", "Disable repeat"));
        }
        else{
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat, "title", "Enable repeat"));
        }

        // Play
        WebElement wPlay = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[3]"));
        if(wPlay.getAttribute("aria-label").equals("Pause")) {
            mainPage.togglePlayButton();
            wait.until(ExpectedConditions.attributeContains(wPlay, "aria-label","Play"));
        }
        else{
            mainPage.togglePlayButton();
            wait.until(ExpectedConditions.attributeContains(wPlay,"aria-label","Pause"));
        }
    }

    @AfterTest
    public void closeBrowser(){
        webDriver.quit();
    }

}
