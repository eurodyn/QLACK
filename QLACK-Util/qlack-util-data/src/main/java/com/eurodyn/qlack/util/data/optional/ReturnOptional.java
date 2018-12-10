package com.eurodyn.qlack.util.data.optional;

import com.eurodyn.qlack.common.exceptions.QDoesNotExistException;
import com.google.common.collect.Iterables;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * A convenience class to return the wrapped optional value throwing an exception when such value does not exist.
 */
public class ReturnOptional {

  private static <T> T r(Optional<T> arg, String message) {
    if (arg != null && arg.isPresent()) {
      return arg.get();
    } else {
      throw new QDoesNotExistException(message);
    }
  }

  /**
   * Returns the wrapped value or throws an exception if such value does not exist.
   * @param arg The optional value.
   * @param params The list of parameters used when fetching this optional to provide a better exception log.
   */
  public static <T> T r(Optional<T> arg, Object... params) {
    return r(arg, MessageFormat.format("Did not find object with parameters {0}.", params));
  }

  /**
   * Returns the wrapped value or throws an exception if such value does not exist.
   * @param arg The optional value.
   * @param params The list of parameters used when fetching this optional to provide a better exception log.
   */
  public static <T> T r(Optional<T> arg, Iterable<Object> params) {
    return r(arg, MessageFormat.format("Did not find object with parameters {0}.", Iterables.toString(params)));
  }
}
