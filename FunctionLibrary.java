package CommonFunctionalLibrary;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utilities.PropertyFileUtil;


public class FunctionLibrary
{
	public static WebDriver startBrowser(WebDriver driver) throws Throwable, Throwable
	{
		if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("Firefox"))
		{
			driver = new FirefoxDriver();
		}
		else
			if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("Chrome"))
			{
				System.setProperty("webdriver.chrome.driver", "CommonJarFiles/chromedriver.exe");
				driver = new ChromeDriver();
			}
			else
				if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("IE"))
				{
					System.setProperty("webdriver.ie.driver", "CommonJarFiles/IEDriverServer.exe");
					driver = new InternetExplorerDriver();
				
		}
		return driver;
	
	}

	public static void openApplication(WebDriver driver) throws Throwable, Throwable
	{
		driver.manage().window().maximize();
		driver.get(PropertyFileUtil.getValueForKey("URL"));
	}
	public static void CloseApplication(WebDriver driver)
	{
		driver.close();
	}
	public static void clickAction(WebDriver driver,String locatorType,String locatorValue)
	{
		if(locatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorValue)).click();
		}
		else
			if(locatorType.equalsIgnoreCase("name"))
			{
				driver.findElement(By.name(locatorValue)).click();
			}
			else
				if(locatorType.equalsIgnoreCase("xpath"))
				{
					driver.findElement(By.xpath(locatorValue)).click();
				}	
	}
	public static void typeAction(WebDriver driver,String locatorType,String locatorValue,String data){
	if(locatorType.equalsIgnoreCase("id")){
		driver.findElement(By.id(locatorValue)).clear();
		driver.findElement(By.id(locatorValue)).sendKeys(data);
		
	}
	else
		if(locatorType.equalsIgnoreCase("name")){
			driver.findElement(By.name(locatorValue)).clear();
			driver.findElement(By.name(locatorValue)).sendKeys(data);
			
		}
		else
			if(locatorType.equalsIgnoreCase("xpath")){
				driver.findElement(By.xpath(locatorValue)).clear();
				driver.findElement(By.xpath(locatorValue)).sendKeys(data);
	}
	}

public static void waitForElement(WebDriver driver,String locatorType,String locatorValue,String waittime){
	WebDriverWait wait = new WebDriverWait(driver,Integer.parseInt(waittime) );
if(locatorType.equalsIgnoreCase("id"))
{
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorValue)));
}
else
	if(locatorType.equalsIgnoreCase("name"))
	{
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorValue)));
	}
	
	else
		if(locatorType.equalsIgnoreCase("xpath"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorValue)));
		}
}
public static void titleValidation(WebDriver driver, String exp_title )
{
	String act_title = driver.getTitle();
	  Assert.assertEquals(act_title, exp_title);	
}
public static String generateDate()
{
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_ss");
			return sdf.format(date);
}
public static void captureData(WebDriver driver, String locatorType, String locatorValue) throws Throwable
{
	String data = "";
	
	if(locatorType.equalsIgnoreCase("id"))
	{
		data = driver.findElement(By.id(locatorValue)).getAttribute("value");
	}
	else
		if(locatorType.equalsIgnoreCase("xpath"))
		{
			data = driver.findElement(By.xpath(locatorValue)).getAttribute("Value");
		}
	
	FileWriter fr = new FileWriter("./CapturedData/Data.txt");
	
	BufferedWriter br = new BufferedWriter(fr);
	
	br.write(data);
	
	br.flush();
	
	br.close();		
}


public static void tableValidation(WebDriver driver, String colNum) throws Throwable
{
	FileReader fr = new FileReader("./CapturedData/Data.txt");
	
	BufferedReader br = new BufferedReader(fr);
	
	String exp_data = br.readLine();
	
	// String to int
	int colNum1 = Integer.parseInt(colNum);
	
	
	if(driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).isDisplayed())
	{
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).clear();
	
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).sendKeys(exp_data);
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.btn"))).click();
		
		Thread.sleep(2000);
	}
	else
	{
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.panel"))).click();
		
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).clear();
		
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).sendKeys(exp_data);
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.btn"))).click();
		
		Thread.sleep(2000);
	}
	
	
	WebElement webtable = driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Webtable.path")));
	
	List<WebElement> rows = webtable.findElements(By.tagName("tr"));
	
	for(int i=1;i<=rows.size();i++)
	{
		String act_data = driver.findElement(By.xpath(".//*[@id='ewContentColumn']/div[3]/form/div/div//table[@class='table ewTable']/tbody/tr["+i+"]/td["+colNum1+"]/div/span/span")).getText();
	
		Assert.assertEquals(act_data, exp_data);
		
		break;
	}
}
public static void mouseover(WebDriver driver,String Locator_Value) throws Throwable
{
Actions action= new Actions(driver);
WebElement element = driver.findElement(By.xpath(Locator_Value));
action.moveToElement(element).build().perform();
Thread.sleep(2000);
}
public static void StocktableValidation(WebDriver driver, String colNum ,String exp_data) throws Throwable
{
	
	int colNum1 = Integer.parseInt(colNum);
	
	
	if(driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).isDisplayed())
	{
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).clear();
	
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).sendKeys(exp_data);
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.btn"))).click();
		
		Thread.sleep(2000);
	}
	else
	{
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.panel"))).click();
		
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).clear();
		
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.box"))).sendKeys(exp_data);
		
		Thread.sleep(2000);
		
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Search.btn"))).click();
		
		Thread.sleep(2000);
	}
	
	
	WebElement webtable = driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("Webtable.path")));
	
	List<WebElement> rows = webtable.findElements(By.tagName("tr"));
	
	for(int i=1;i<=rows.size();i++)
	{
		String act_data = driver.findElement(By.xpath(".//*[@id='ewContentColumn']/div[3]/form/div/div//table[@class='table ewTable']/tbody/tr["+i+"]/td["+colNum1+"]/div/span/span")).getText();
	
		Assert.assertEquals(act_data, exp_data);
		
		break;
	}
}
}
	


