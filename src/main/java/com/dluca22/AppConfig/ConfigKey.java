package com.dluca22.AppConfig;

// contract that defines the applicatoin config API
public enum ConfigKey {

  // accepted enum values
  WATCH_DIR(String.class, true, "/data"),
  TARGET_DIR(String.class, false, null),
  DEBOUNCE_TIME(Integer.class, false, 3);

  // private values
  private final Class<?> type;
  private final boolean required;
  private final Object defaultValue;

  // enum Constructor 
  ConfigKey(Class<?> type, boolean required, Object defaultValue){
    this.type = type;
    this.required = required;
    this.defaultValue = defaultValue;
  }

  // GETTERS
  public Class<?> type(){
    return this.type;
  }

  public boolean required(){
    return this.required;
  }

  public String keyName(){
    // inherited built-in method from the enums
    return name();
  }

  public String variableName(){
    // returns the variable name formatted as Java convention
    return name().toLowerCase().replace('_', '.');
  }

  public Object defaultValue() { 
    return defaultValue; 
  }
  
}