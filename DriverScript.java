package DriverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunctionalLibrary.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {
	WebDriver driver;
	ExtentReports reports;
	ExtentTest logger;
	public void startTest() throws Throwable
	{
		ExcelFileUtil excel = new ExcelFileUtil();
		//Module sheet
		for(int i=1;i<=excel.rowCount("MasterTestCases"); i++)
		{
			String ModuleStatus = "";
			if(excel.getData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
			{
				//define module name
			String TCModule = excel.getData("MasterTestCases", i, 1);
			reports = new ExtentReports("./Reports/"+TCModule+".html"+"_"+FunctionLibrary.generateDate());
			logger = reports.startTest(TCModule);
					int rowcount = excel.rowCount(TCModule);
					//corresponding sheets
					for(int j=1;j<=rowcount;j++)
					{
						String Description = excel.getData(TCModule, j, 0);
						String Object_Type = excel.getData(TCModule, j, 1);
						String Locator_Type = excel.getData(TCModule, j, 2);
						String Locator_Value = excel.getData(TCModule, j, 3);
						String Test_Data = excel.getData(TCModule, j, 4);
					try {	
						if (Object_Type.equalsIgnoreCase("startBrowser"))
						{
							driver = FunctionLibrary.startBrowser(driver);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("openApplication"))
						{
							FunctionLibrary.openApplication(driver);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("clickAction"))
						{
							FunctionLibrary.clickAction(driver, Locator_Type, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("typeAction"))
						{
							FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("waitForElement"))
						{
                           FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);
                           logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("captureData"))
						{
							FunctionLibrary.captureData(driver, Locator_Type, Locator_Value);
							
						}
						if(Object_Type.equalsIgnoreCase("tableValidation"))
						{
							FunctionLibrary.tableValidation(driver, Test_Data);
						}
						if(Object_Type.equalsIgnoreCase("closeApplication"))
						{
							FunctionLibrary.CloseApplication(driver);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("mouseOver"))
						{
							FunctionLibrary.mouseover(driver, Locator_Value);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("StocktableValidation"))
						{
							FunctionLibrary.StocktableValidation(driver, Locator_Value, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
						if(Object_Type.equalsIgnoreCase("titleValidation"))
						{
							FunctionLibrary.titleValidation(driver, Test_Data);
							logger.log(LogStatus.INFO, Description);
						}
					excel.setData(TCModule,j,5,"Pass");
					ModuleStatus = "true";
					logger.log(LogStatus.PASS, Description + "Pass");
					}
					catch(Exception e)
					{
						excel.setData(TCModule, j, 5, "Fail");
						ModuleStatus = "false";
						logger.log(LogStatus.FAIL, Description + "Fail");
						
						File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						
						FileUtils.copyFile(scrFile, new File("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".jpg"));
						
						String image = logger.addScreenCapture("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".jpg");
						
						logger.log(LogStatus.FAIL, image);
						
						break;		
					
					}
					catch(AssertionError e)
					{
						excel.setData(TCModule, j, 5, "Fail");
						ModuleStatus = "false";
						logger.log(LogStatus.FAIL, Description + "Fail");
						
						File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                        
						FileUtils.copyFile(scrFile, new File("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".jpg"));
						
						String image = logger.addScreenCapture("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".jpg");
						
						logger.log(LogStatus.FAIL, image);
						
						break;
					}
					
					}
					if(ModuleStatus.equalsIgnoreCase("true"))
					{
						excel.setData("MasterTestCases", i, 3, "Pass");
					}
					else
					{
						excel.setData("MasterTestCases", i, 3, "Fail");	
					}
			}
					
					else
					{
						excel.setData("MasterTestCases", i, 3, "Not Executed");
					}
			reports.endTest(logger);
			reports.flush();
						
					
					
					
			}
		}

		
	}


