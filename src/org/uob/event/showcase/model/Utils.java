package org.uob.event.showcase.model;

/**
 * A utility class.
 *
 */
public final class Utils {

  private Utils() {

  }

  /**
   * Helper method to assert the condition is satisfied.
   *
   * @param condition the condition to assert.
   * @param msg the message if condition is not satisfied.
   */
  public static void assertTrue(boolean condition, String msg) {
    if (!condition) {
      if (msg != null) {
        throw new ESModelException(msg);
      } else {
        throw new ESModelException();
      }
    }
  }
}
