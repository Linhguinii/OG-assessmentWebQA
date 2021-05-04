
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.jetty9.util.log.Log;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO: Don't move the mouse when running test. It may conflict with some tests. As well as close any Spotify application if open.
// @pre: There is at least a few songs in queue to allow subsequent operations && start on Pause(or Play in HTML)
public class BasicFunctionsTest {
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

    @Test
    public void PlayAndPauseTest(){
        // Set up
        By byPlay = mainPage.getByPlay();
        // Wait for the loading of the MainPage
        wait.until(ExpectedConditions.visibilityOfElementLocated(byPlay));
        mainPage.togglePlayButton();
        // Explicit wait for the attribute Pause, else throw ElementNotVisibleException
        WebElement wPlay = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[3]"));
        wait.until(ExpectedConditions.attributeContains(wPlay,"aria-label", "Pause") );
        mainPage.togglePlayButton();
        wait.until(ExpectedConditions.attributeContains(wPlay,"aria-label", "Play") );
    }

    @Test
    public void nextButtonTest(){
        // Set up
        By nextElement = mainPage.getByNext();
        // Explicit wait for page to load Next element
        wait.until(ExpectedConditions.visibilityOfElementLocated(nextElement));
        mainPage.toggleNextButton();
        // Explicit wait for the attribute Pause, else throw ElementNotVisibleException
        WebElement wPlay = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[3]"));
        wait.until(ExpectedConditions.attributeContains(wPlay,"aria-label", "Pause") );

    }

    @Test
    /** We can only "properly" test the Previous button with the following @pre condition
     * @pre: there's a song before the current song
     */
    public void previousButtonTest(){
        // Set up
        By byPrevious = mainPage.getByPrevious();
        // Wait for the loading of the MainPage
        wait.until(ExpectedConditions.visibilityOfElementLocated(byPrevious));

        WebElement songTimeElement = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div/div[2]/div/div[2]/div[1]"));
        // Two different states: from the start and else
        // Start
        WebElement playElement = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[3]"));
        if(songTimeElement.getText().equals("0:00")){
            mainPage.togglePreviousButton();
            try{
                // Explicit wait for the attribute Pause, else throw ElementNotVisibleException
                wait.until(ExpectedConditions.attributeContains(playElement, "aria-label", "Pause"));
            }
            catch(TimeoutException e){
                System.out.println("We are at the beginning of the playlist.");
            }
        }
        else{
            mainPage.togglePreviousButton();
            wait.until(ExpectedConditions.textToBePresentInElement(songTimeElement, "0:00"));
            assertEquals("0:00", songTimeElement.getText());
        }
    }

    @Test
    public void shuffleButtonTest(){
        // Set up
        By byShuffle = mainPage.getByShuffle();
        // Wait for the loading of the MainPage
        wait.until(ExpectedConditions.visibilityOfElementLocated(byShuffle));

        WebElement wShuffle = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[1]"));
        // Shuffle has 2 states: Enable shuffle, disable shuffle
        // Enable shuffle
        if(wShuffle.getAttribute("title").equals("Enable shuffle")){
            mainPage.toggleShuffleButton();
            wait.until(ExpectedConditions.attributeContains(wShuffle, "title", "Disable shuffle"));
        }
        else{
            mainPage.toggleShuffleButton();
            wait.until(ExpectedConditions.attributeContains(wShuffle, "title", "Enable shuffle"));
        }
    }

    @Test
    public void repeatButtonTest() {
        // Set up
        By byRepeat = mainPage.getByRepeat();
        // Wait for the loading of the MainPage
        wait.until(ExpectedConditions.visibilityOfElementLocated(byRepeat));

        WebElement wRepeat = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[5]"));
        // Repeat has 3 states: Enable repeat, Enable repeat one, Disable repeat
        if(wRepeat.getAttribute("title").equals("Enable repeat")){
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat,"title", "Enable repeat one"));
        }
        else if(wRepeat.getAttribute("title").equals("Enable repeat one")){
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat, "title", "Disable repeat"));
        }
        else{
            mainPage.toggleRepeatButton();
            wait.until(ExpectedConditions.attributeContains(wRepeat,"title","Enable repeat"));
        }

    }

    @Test
    // Testing progress bar to 0.00
    public void progressbarTest(){
        // Set up
        By byProgressBar = mainPage.getByProgressBar();
        // Wait for the loading of the MainPage
        wait.until(ExpectedConditions.visibilityOfElementLocated(byProgressBar));
        mainPage.setProgressBar(0);
        WebElement wSongTime = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div/div[2]/div/div[2]/div[1]"));
        assertEquals("0:00", wSongTime.getText());
    }

    @AfterTest
    public void closeBrowser(){
        webDriver.quit();
    }
}
