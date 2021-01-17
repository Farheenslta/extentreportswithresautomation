package extentreportspack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ExtentReportWithResultsAutomationForFacebook
{
	public static void main(String[] args) throws Exception
	{
		//Create extent-report object
		ExtentReports er=new ExtentReports("facebookextentreportsres.html", false);
		ExtentTest et=er.startTest("Facebook Title Test Results");
		//Open browser
		WebDriverManager.chromedriver().setup();
		ChromeDriver driver=new ChromeDriver();
		//maximize browser
		driver.manage().window().maximize();
		//launch site
		driver.get("https://www.facebook.com/");
		//create Wait object
		WebDriverWait wait=new WebDriverWait(driver,20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//*[text()='Log In'])[1]")));
		String title=driver.getTitle();
		if(title.contains("Facebook"))
		{
			et.log(LogStatus.PASS, "Facebook Title Test passed");
		}
		else
		{
			//Screenshot
			SimpleDateFormat sf=new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
			Date dt=new Date();
			String fname=sf.format(dt)+".png";
			File src=driver.getScreenshotAs(OutputType.FILE);
			File dest=new File(fname);
			FileHandler.copy(src, dest);
			et.log(LogStatus.FAIL, et.addScreenCapture(fname), "Facebook Title Test failed");
		}
		//close site
		driver.close();
		
		//Save extent report file
		er.endTest(et);
		er.flush();
		er.close();
		
		//Automating results file
		ChromeDriver driver1=new ChromeDriver();
		driver1.get(System.getProperty("user.dir")+"\\facebookextentreportsres.html");
		driver1.manage().window().maximize();
		List<WebElement> l=driver1.findElements(By.xpath("//*[@id='test-collection']/li"));
		Thread.sleep(3000);
		int size=l.size();
		l.get(size-1).click();
		Thread.sleep(3000);
		String x=driver1.findElement(By.xpath("//*[@id='test-collection']/li["+size+"]/div/span[2]")).getText();
		x=x.toLowerCase();
		if(x.contains("pass"))
		{
			driver1.findElement(By.xpath("//*[@id='slide-out']/li[2]/a/i")).click();
		}
		else
		{
			try
			{
				driver1.findElement(By.xpath("(//*[@class='report-img'])[2]")).click();
			}
			catch(Exception ex)
			{
			}
		}
	}
}
