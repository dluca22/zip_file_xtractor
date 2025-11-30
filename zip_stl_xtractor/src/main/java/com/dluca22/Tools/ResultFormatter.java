package com.dluca22.Tools;

public record ResultFormatter(boolean success, String name) {
  public static ResultFormatter success(String name) {
    return new ResultFormatter(true, name);
  }

  public static ResultFormatter failure() {
    return new ResultFormatter(false, "");
  }
}
