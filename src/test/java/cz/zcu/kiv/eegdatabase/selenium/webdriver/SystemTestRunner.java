package cz.zcu.kiv.eegdatabase.selenium.webdriver;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * User: Maverick
 */

/**
 * Runner of system tests
 */
public class SystemTestRunner {
  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(SystemTestSuite.class);
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
    System.out.println(result.wasSuccessful());
  }
}

