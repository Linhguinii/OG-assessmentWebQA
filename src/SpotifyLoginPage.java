import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SpotifyLoginPage {
    private WebDriver aDriver;
    private final By aUsername = By.name("username");
    private final By aPassword = By.name("password");
    private final By aLogin = By.id("login-button");

    public SpotifyLoginPage(WebDriver pDriver){
        aDriver = pDriver;
    }

    public By getByUsername(){
        return aUsername;
    }

    public By getByPassword(){
        return aPassword;
    }

    public By getByLogin(){
        return aLogin;
    }

    private void setUserName(String pUsername){
        aDriver.findElement(aUsername).sendKeys(pUsername);
    }

    private void setPassword(String pPassword){
        aDriver.findElement(aPassword).sendKeys(pPassword);
    }

    private void clickLogin(){
        aDriver.findElement(aLogin).click();
    }

    /**
     * Sequence of methods to login to Spotify
     */
    public void loginToSpotify(String pUsername , String pPassword){
        this.setUserName(pUsername);
        this.setPassword(pPassword);
        this.clickLogin();
    }
}
