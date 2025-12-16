package com.dluca22.DirectoryWatcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryWatcher {

  private Path directory;
  WatchService watcher;

  public DirectoryWatcher() {
  }

  public DirectoryWatcher(Path directory) {
    this.directory = directory;
  }
  
  public void init() {
    try {
      watcher = FileSystems.getDefault().newWatchService();

      WatchKey key = this.directory.register(this.watcher,ENTRY_CREATE,
                                                          ENTRY_MODIFY
                                                          // ENTRY_DELETE,
                                                        );
    } catch (IOException x) {
      System.err.println(x);
    }
  }
}
