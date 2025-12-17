package com.dluca22.MioLogger;

import com.dluca22.DirectoryWatcher.DirectoryEventListener;

public class MioLogger implements DirectoryEventListener {


  public void log(String msg){
    System.out.println(msg);
  }

  @Override
  public void onContentChanged(String eventName) {
    this.log(String.format("Event %s.", eventName));
    
  }
}
