import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.lang.reflect.Field;

// TODO: Don't move the mouse when running test. It may conflict with some tests. As well as close any Spotify application if open.
// @pre: There is at least a few songs in queue to allow subsequent operations && start on Pause(or Play in HTML)
public class BonusTest {
    WebDriver webDriver;
    WebDriverWait wait;
    String url;
    SpotifyMainPage mainPage;
    SpotifyStartPage startPage;
    @BeforeTest
    public void setup(){
        // TODO: local chrome driver
        System.setProperty("webdriver.chrome.driver","C:\\Users\\ToFoo\\Downloads\\chromedriver_win32\\chromedriver.exe");
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
        loginPage.loginToSpotify("tofoo_power@hotmail.com","06##GXVzQt6X");
    }

    public void clearCookie(){
        By cookieButton = startPage.getByCookieButton();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cookieButton));
        startPage.clickCookieExitButton();
    }

    /**
     * Proper "previous" test compared to BasicFunctionsTest. Compare song and album titles + use other basic functions
     */
    @Test
    public void BonusTest1(){
        // Set up
        WebElement wPlay = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[3]"));
        // Make sure its on repeat so we have songs to go through
        WebElement wRepeat = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[5]"));
        if(wRepeat.getAttribute("title").equals("Enable repeat")){
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeToBe(wRepeat, "aria-label", "Enable repeat one"));
        }
        else if(wRepeat.getAttribute("title").equals("Disable repeat")){
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeToBe(wRepeat, "aria-label","Enable repeat"));
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeToBe(wRepeat, "aria-label","Enable repeat one"));
        }

        // Get INITIAL song
        WebElement songElement = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[1]/div/div[2]/div[1]/span/a"));
        String songAlbum = songElement.getAttribute("href");
        String songName = songElement.getText();

        // Get initial state of the time in the song
        mainPage.togglePlayButton();
        wait.until(ExpectedConditions.attributeToBe(wPlay,"aria-label","Pause"));
        mainPage.togglePlayButton();
        wait.until(ExpectedConditions.attributeToBe(wPlay,"aria-label","Play"));

        WebElement songTimeElement = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div/div[2]/div/div[2]/div[1]"));
        // If song is not at the start: toggling previous will go to zero
        if(!songTimeElement.getText().equals("0:00")){
            mainPage.togglePreviousButton();
        }
        // If song is at the start: toggling previous will directly go to the previous song
        // Click previous button
        mainPage.togglePreviousButton();
        // Wait for Spotify to register input and start previous song
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementValue(songElement, songName)));
        wait.until(ExpectedConditions.attributeContains(wPlay, "aria-label", "Pause"));
        // Pause
        mainPage.togglePlayButton();
        // Get PREVIOUS song
        //WebElement songElement_Prev = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[1]/div/div[2]/div[1]/span/a"));
        String songAlbum_Prev = songTimeElement.getAttribute("href");
        String songName_Prev = songTimeElement.getText();
        // Test verification
        assertThat(songName, is(not(songName_Prev)));
        assertThat(songAlbum, is(not(songAlbum_Prev)));

    }

    @AfterTest
    public void closeBrowser(){
        webDriver.quit();
    }
}
