package com.vehiclecheck.test.helpers;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class WebDriverHelper {

    public static RemoteWebDriver REAL_DRIVER;
    @Getter
    public static String url;
    public static WebDriverWait wait;
    public  static Properties prop;
    private static final Thread CLOSE_THREAD= new Thread(() -> {
        log.info("Driver Quit on Shutdown Hook");
        quitWebDriver();
    });

    static {
        loadRunConfigProps();
        url = prop.getProperty("url");
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }


    public static void loadRunConfigProps() {
        prop=new Properties();
        try {
            ClassLoader classLoader = WebDriverHelper.class.getClassLoader();
            FileInputStream fileinputStream = new FileInputStream(new File(Objects.requireNonNull(classLoader.getResource("config.properties")).getFile()).getAbsolutePath());

            //FileInputStream fileinputStream = new FileInputStream(new File(classLoader.getResource("config.properties").getFile()).getAbsolutePath());
            prop.load(fileinputStream);
        }catch (Exception e){
            log.error("Exception while loading the properties");
        }
    }

    public static void quitWebDriver() {
        if (REAL_DRIVER != null) {
            REAL_DRIVER.quit();
            REAL_DRIVER = null;
        }
    }

    public static WebDriver getWebDriver() {
        if (REAL_DRIVER == null) {
            startDriver();
            wait = null;
        }
        return REAL_DRIVER;
    }

    public static void startDriver() {
        REAL_DRIVER = startChromeDriver();
        REAL_DRIVER.manage().deleteAllCookies();
        REAL_DRIVER.navigate().refresh();
        REAL_DRIVER.manage().window().maximize();
    }
    private static ChromeDriver startChromeDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(getChromeOptions());
    }
    private static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--disable-web-security");
        chromeOptions.addArguments("--test-type");
        chromeOptions.addArguments("allow-running-insecure-content");
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return chromeOptions;
    }
}
