package com.iipsen2.app.helpers;

import com.iipsen2.app.MainService;
import io.github.cdimascio.dotenv.Dotenv;

public class CoreHelper {
  protected static Dotenv getEnv() {
    return MainService.env;
  }
}
