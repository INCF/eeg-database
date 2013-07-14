package cz.zcu.kiv.eegdatabase.data.dao;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * User: Maverick
 */

/**
 * Runner of tests for data Layer
 */
public class DaoTestRunner {
    public static void main(String[] args) {
      Result result = JUnitCore.runClasses(DaoTestSuite.class);
      for (Failure failure : result.getFailures()) {
        System.out.println(failure.toString());
      }
      System.out.println(result.wasSuccessful());
    }
}
