package com.dluca22.Tools;

import java.io.IOException;
import java.nio.file.Path;

public class Utils {
  public static double convertFromBytes(long bytes, String unit) {
    int exponent;
    switch (unit.toUpperCase()) {
      case "KB": exponent = 1;break;
      case "MB": exponent = 2; break;
      case "GB": exponent = 3; break;
      default: exponent = 0; break; // default return bytes
    }

    return bytes / Math.pow(1024, exponent);
  }

  public static long convertToBytes(double value, String unit) {
    int exponent;
    switch (unit.toUpperCase()) {
      case "KB": exponent = 1; break;
      case "MB": exponent = 2; break;
      case "GB": exponent = 3; break;
      default: exponent = 0; break;
    }
    return Math.round(value * Math.pow(1024, exponent));
  }

  public static boolean isSameDirectory(Path original, Path target ) throws IOException{
    Path originalRealPath = original.toRealPath();
    Path targetRealPath = target.toRealPath();

    return originalRealPath.equals(targetRealPath);
  }

}
