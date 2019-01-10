**Katalon automatic systemtests**
_____________________________

Due to the fact, that the feedback tool concentrates on a website as an UI it is possible to use Website testing mechanisms like Selenium (or a 3rd party solution like Katalon in our case because of compatibility issues with Selenium Support for Firefox Version higher than 55)

How to run this Tests / import them:

Prequisites: Firefox Browser (newest version 64), IDE with JUnit Runtime installed, Geckodriver (available at https://github.com/mozilla/geckodriver/releases)

*These tests are captured with Katalon, a popular browser extension for Google Chrome and exported in Java Source Files.
All Tests are compatible to IDEs with a JUnit Runtime Environment installed (e.g. STS, Eclipse...).*

Step 1. Create a Maven Project in your Eclipse.
If you haven't done this before, see:

How to install/integrate Maven with Eclipse (https://stackoverflow.com/questions/8722835/setting-up-new-maven-in-eclipse/18494075#18494075)
How to create a simple Maven project (https://wiki.jasig.org/display/UPM32/Creating+a+Simple+Maven+Project)

Step 2. Add the following dependencies to your pom.xml file:

```
<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.7</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.seleniumhq.selenium</groupId>
			<artifactId>selenium-java</artifactId>
			<version>3.141.59</version>
		</dependency>
		<dependency>
			<groupId>org.seleniumhq.selenium</groupId>
			<artifactId>selenium-firefox-driver</artifactId>
			<version>3.141.59</version>
		</dependency>
		<dependency>
			<groupId>org.seleniumhq.selenium</groupId>
			<artifactId>selenium-server</artifactId>
			<version>3.141.59</version>
</dependency>
```

Step 3. Copy this Java files and the Geckodriver (same level as pom.xml) into the Maven Project.

Step 4. Add the following (and other IDE - highlighted) imports to the file (if needed):
```
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
```

Step 5. Add (if needed) the following line of code to your tests above the driver initialisation
```
System.setProperty("webdriver.gecko.driver", "geckodriver");
```

and change the path and file suffix like its named in the file system

Step 6. Run the tests with Run As.. --> JUnit Test
