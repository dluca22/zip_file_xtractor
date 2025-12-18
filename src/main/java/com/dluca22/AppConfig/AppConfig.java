package com.dluca22.AppConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

// config class to inject
public final class AppConfig {

  // map to store env variables by key
  private static final Map<ConfigKey, Object> values = new EnumMap<>(ConfigKey.class);

  // on init load properties and run check for the required values
  static {
    loadProperties();
    checkRequired();
  }

  private static void loadProperties() {
    /*
     * The Properties class represents a persistent set of properties.
     * The Properties can be saved to a stream or loaded from a stream.
     * Each key and its corresponding value in the property list is a string.
     */
    Properties properties = new Properties();

    try (InputStream inputStream = AppConfig.class
        .getClassLoader() // the class loader that loaded the class or interface represented by this Class
                          // object.
        .getResourceAsStream("application.properties") // load the file as a stream
    ) {
      // read the env file application.properties from the inputStream
      if (inputStream != null) {
        properties.load(inputStream);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed while loading properties", e);
    }

    // for each key configured in the ConfigKeys enum, try to get the key from ENV
    // variable (i.e. from docker)
    // else try to fetch it from the application.properties (like a default fallback
    // if no ENV present)
    for (ConfigKey key : ConfigKey.values()) {
      String raw = System.getenv(key.keyName());
      if (raw == null) {
        raw = properties.getProperty(key.variableName());
      }

      // if found the key, assign to the class values map
      if (raw != null) {
        Object parsed = parseConfigVariable(raw, key.type()); // parse the values based on the type defined in the enum
        values.put(key, parsed);
      } // if key still not provided fallback to default value if not null
      else if (key.defaultValue() != null) {
        values.put(key, key.defaultValue());
      }
    }
  }

  // based on the type declared in the ConfigKeys enum, return the parsed value
  private static Object parseConfigVariable(String keyName, Class<?> type) {
    if (type == Integer.class)
      return Integer.parseInt(keyName);
    if (type == Boolean.class)
      return Boolean.parseBoolean(keyName);
    return keyName;
  }

  // validate the key enum key is required and actually present or at least has
  // defaultValue
  private static void checkRequired() {
    for (ConfigKey key : ConfigKey.values()) {
      if (key.required() && key.defaultValue() == null && !values.containsKey(key)) {
        throw new IllegalStateException("Missing required configuration key: " + key.keyName());
      }
    }
  }

  // helper getters to avoid exposing the values map directly
  // returns
  public static String getString(ConfigKey key) {
    Object value = values.get(key);
    return (String) value;
  }

  public static int getInt(ConfigKey key) {
    Object value = values.get(key);
    return (Integer) value;
  }

  public static boolean getBoolean(ConfigKey key) {
    Object value = values.get(key);
    return (Boolean) value;
  }

  public static boolean isPresent(ConfigKey key) {
    return values.containsKey(key);
  }
}