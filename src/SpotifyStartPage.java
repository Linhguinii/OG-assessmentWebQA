import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SpotifyStartPage {
    private WebDriver aDriver;
    private By aLogin = By.xpath("//*[@id=\"main\"]/div/div[2]/div[1]/header/div[5]/button[2]");
    private final By aCookieButton = By.xpath("//*[@id=\"onetrust-close-btn-container\"]/button");

    public SpotifyStartPage(WebDriver pDriver){
        aDriver = pDriver;
    }

    public By getByCookieButton(){
        return aCookieButton;
    }

    public void clickCookieExitButton(){
        aDriver.findElement(aCookieButton).click();
    }

    private void clickLogin(){
        aDriver.findElement(aLogin).click();
    }

    public void clickSpotifyLoginPage(){
        this.clickLogin();
    }



}
