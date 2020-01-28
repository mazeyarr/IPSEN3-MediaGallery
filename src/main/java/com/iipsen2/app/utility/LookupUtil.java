package com.iipsen2.app.utility;

import com.iipsen2.app.services.ExceptionService;

import javax.ws.rs.core.Response;

public class LookupUtil {
  public static <E extends Enum<E>> boolean lookup(Class<E> e, String id) {
    try {
      Enum.valueOf(e, id);

      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}
