package com.dluca22.AppConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

public class AppConfigTest {
  
  @Test
  @DisplayName("Test whatch.dir parameter matches the expected output")
  void TestConfigParameterWatchDirIsCorrect() {
    String expectedValue = "/data";
    String value = AppConfig.getString(ConfigKey.WATCH_DIR);
    assertEquals(expectedValue, value);
  }
  
  @Test
  @DisplayName("Test debounce parameter is present")
  void TestDebounceParameterIsPresent() {
    int debounceTime = AppConfig.getInt(ConfigKey.DEBOUNCE_TIME);
   
    assertNotNull(debounceTime);
  }
  
  @Test
  @DisplayName("Test debounce parameter is correct")
  void TestDebounceParameterIsCorrect() {
    int expectedValue = 3;
    int debounceTime = AppConfig.getInt(ConfigKey.DEBOUNCE_TIME);
    assertEquals(expectedValue, debounceTime);
  }

  @Test
  @DisplayName("Test WATCH_DIR parameter is present")
  void TestWatchDirIsPresent() {
    assertTrue(AppConfig.isPresent(ConfigKey.WATCH_DIR));
  }

  @Test
  @DisplayName("Test TARGET_DIR parameter is not present") // it should fail if is provided in the application.properties
  void TestTargetDirIsNotPresent() {
    assertFalse(AppConfig.isPresent(ConfigKey.TARGET_DIR));
  }

  @Test
  @DisplayName("Test TARGET_DIR parameter is present") // it should pass if provided in application.properties
  void TestTargetDirIsPresent() {
    assertFalse(AppConfig.isPresent(ConfigKey.TARGET_DIR));
  }
}
