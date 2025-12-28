package com.dluca22.Playground;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Playground {
  public static void main(String[] args) {
    // testPrint();

    // doFileTests();
    checkPathsEquality();
  }

  static void checkPathsEquality() {
    Path source = Paths.get("sourceDir");
    Path dest = Paths.get("sourceDir");

    System.out.println("Root? " + source.getRoot());
    System.out.println("Parent? " + source.getParent());
    System.out.println("Are equal? " + source.equals(dest));

    System.out.println("Absolute source: " + source.toAbsolutePath());
    System.out.println("Absolute dest: " + dest.toAbsolutePath());
  }

  static void doFileTests() {
    System.out.println("CWD: " + System.getProperty("user.dir"));
    Path testFile = Paths.get("sourceDir/Pepe_keycap.zip");
    System.out.println("File exists: " + Files.exists(testFile));
    System.out.println("File isDir: " + Files.isDirectory(testFile));

    // printFileDimensions(testFile);
  }

  static void printFileDimensions(Path testFile) {
    try {
      long fileSize = Files.size(testFile);
      test2FilesDimesions(fileSize);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  static void test2FilesDimesions(long fileBytes) {
    // dimesnioni file
    System.out.println("File dimension: " + fileBytes + "bytes");

    // // da dimensinoi file a Mb
    // System.out.println("fileBytes to MB : " + convertFromBytes(fileBytes, "MB"));
    // // standard 1500000 bytes dovrebbero essere 1.5 MB
    // System.out.println("1500000 to MB : " + convertFromBytes(1_500_000, "MB"));

    // // quanto è 1.5 MB to bytes
    // System.out.println("1.5 to MB : " + convertToBytes(1.5, "MB"));
    // // quanto è 1.5026044845581055 in bytes
    // System.out.println("1.5026044845581055 to MB : " +
    // convertToBytes(1.5026044845581055, "MB"));

    double mb = convertFromBytes(fileBytes, "MB"); // 1.4305 MB
    long bytesBack = convertToBytes(mb, "MB"); // 1_500_000 bytes

  }

  static void testPrint() {
    System.out.println("helo");
  }

  static double convertFromBytes(long bytes, String unit) {
    int exponent = 1;
    switch (unit.toUpperCase()) {
      case "KB":
        exponent = 1;
        break;
      case "MB":
        exponent = 2;
        break;
      case "GB":
        exponent = 3;
        break;
      default:
        break;
    }
    return bytes / Math.pow(1024, exponent);
  }

  static long convertToBytes(double bytes, String originalUnit) {
    int exponent = 1;
    switch (originalUnit.toUpperCase()) {
      case "KB":
        exponent = 1;
        break;
      case "MB":
        exponent = 2;
        break;
      case "GB":
        exponent = 3;
        break;
      default:
        break;
    }
    return Math.round(bytes * Math.pow(1024, exponent));
  }

  // static double convertFromBytes(long bytes, String unit) {
  // int exponent;
  // switch(unit.toUpperCase()) {
  // case "KB": exponent = 1; break;
  // case "MB": exponent = 2; break;
  // case "GB": exponent = 3; break;
  // default: exponent = 0; break; // bytes
  // }
  // return bytes / Math.pow(1024, exponent);
  // }

  // static long convertToBytes(double value, String unit) {
  // int exponent;
  // switch(unit.toUpperCase()) {
  // case "KB": exponent = 1; break;
  // case "MB": exponent = 2; break;
  // case "GB": exponent = 3; break;
  // default: exponent = 0; break; // bytes
  // }
  // return Math.round(value * Math.pow(1024, exponent));
  // }

}

// public enum UnitEnum {
// KB,
// MB,
// GB
// };