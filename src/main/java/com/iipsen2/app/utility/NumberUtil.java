package com.iipsen2.app.utility;

import java.util.Random;

public class NumberUtil {
  public static boolean isLong(String string) {
    if (string == null) {
      return false;
    }

    try {
      Long.parseLong(string);

      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static int getRandom(int from, int to) {
    if (from < to)
      return from + new Random().nextInt(Math.abs(to - from));
    return from - new Random().nextInt(Math.abs(to - from));
  }
}
