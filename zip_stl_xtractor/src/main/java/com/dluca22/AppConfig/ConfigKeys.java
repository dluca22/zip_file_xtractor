package com.dluca22.AppConfig;

// contract that defines the applicatoin config API
public enum ConfigKeys {

  // accepted enum values
  WATCH_DIR(String.class, true),
  TARGET_DIR(String.class, false),
  DEBOUNCE_TIME(Integer.class, false);

  // private values
  private final Class<?> type;
  private final boolean required;

  // enum Constructor 
  ConfigKeys(Class<?> type, boolean required){
    this.type = type;
    this.required = required;
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
}