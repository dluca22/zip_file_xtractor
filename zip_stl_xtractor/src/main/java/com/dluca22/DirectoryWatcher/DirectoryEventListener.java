package com.dluca22.DirectoryWatcher;

import java.nio.file.Path;

public interface DirectoryEventListener {
  // public void onFileCreated(Path path);
  // public void onFileChanged(Path path);
  // public void onFileDeleted(Path path);
  public void onContentChanged(String eventName);

}
