package com.dluca22.AppConfig;

// contract that defines the applicatoin config API
public enum ConfigKeys {
  WATCH_DIR(String.class, true),
  TARGET_DIR(String.class, false),
  DEBOUNCE_TIME(Integer.class, false);

  private final Class<?> type;
  private final boolean required;

  ConfigKeys(Class<?> type, boolean required){
    this.type = type;
    this.required = required;
  }

  public Class<?> type(){
    return this.type;
  }

  public boolean required(){
    return this.required;
  }

  public String envVarName(){
    return name();
  }

  public String propName(){
    return name().toLowerCase().replace('_', '.');
  }
}