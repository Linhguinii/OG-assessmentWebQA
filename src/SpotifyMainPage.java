import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import javax.lang.model.element.Element;
import java.sql.Driver;

public class SpotifyMainPage {
    private final WebDriver aDriver;
    private final By aPlay = By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[3]");
    private final By aNext = By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[4]");
    private final By aPrevious = By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[2]");
    private final By aShuffle = By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[1]");
    private final By aRepeat = By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div[1]/div[2]/div/div[1]/button[5]");
    private final By aProgressBar = By.xpath("//*[@id=\"main\"]/div/div[2]/div[2]/footer/div/div[2]/div/div[2]/div[2]/div");

    public SpotifyMainPage(WebDriver pDriver){
        aDriver = pDriver;
    }

    /**
     * Method to click the play button of the music player
     * 2 states : allows to play and pause the music player
     */
    public void togglePlayButton(){
        aDriver.findElement(aPlay).click();
    }

    public void toggleNextButton(){
        aDriver.findElement(aNext).click();
    }

    public void togglePreviousButton(){
        aDriver.findElement(aPrevious).click();
    }

    /**
     * 2 states : shuffle on, shuffle off
     */
    public void toggleShuffleButton(){
        aDriver.findElement(aShuffle).click();
    }

    /**
     * 3 states: repeat, repeat 1, disable repeat
     */
    public void toggleRepeatButton(){
        aDriver.findElement(aRepeat).click();
    }

    /**
     * Set the progress bar of the current song according to a double percentage
     * Format : for example, 0.75. Not 75.0.
     * @pre >= 0.00
     * @post <= 1.00
     * @param pPercentage
     */
    public void setProgressBar(double pPercentage){
        assert pPercentage >= 0.00 && pPercentage <= 1.00;
        WebElement progress_bar = aDriver.findElement(aProgressBar);
        int width = progress_bar.getSize().width;
        double percentage = pPercentage;
        double progressBar_x = width*percentage;
        double progressBarOffset = width/2;
        // Adjust x-coordinate to be offset from center
        new Actions(aDriver).moveToElement(progress_bar, (int) (progressBar_x-progressBarOffset), 0).click().build().perform();
    }
    
    /**
     * Getters
     * @return the according By
     */
    public By getByPlay(){
        return aPlay;
    }

    public By getByNext(){
        return aNext;
    }

    public By getByPrevious(){
        return aPrevious;
    }

    public By getByShuffle(){
        return aShuffle;
    }

    public By getByRepeat(){
        return aRepeat;
    }

    public By getByProgressBar(){
        return aProgressBar;
    }


}
