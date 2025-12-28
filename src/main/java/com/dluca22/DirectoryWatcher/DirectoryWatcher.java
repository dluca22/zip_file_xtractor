package com.dluca22.DirectoryWatcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.dluca22.AppConfig.AppConfig;
import com.dluca22.AppConfig.ConfigKey;

import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryWatcher {
  long lastExecutionTimestamp;
  private Path directory;
  WatchService watcher;
  private final List<DirectoryEventListener> listeners = new ArrayList<>();

  // empty constructor
  public DirectoryWatcher() {
  }

  // class constructor
  public DirectoryWatcher(Path directory) {
    System.out.println(String.format("DirectoryWatcher constructor ln 30: directory is %s", directory));

    this.directory = directory;
  }

  // method to register listeners of this class' events
  public void AddEventListner(DirectoryEventListener listener) {
    this.listeners.add(listener);
  }

  // initialize starts a new Filesystem watcher and starts the loop
  public void init() {
    try {
      // Watchservice instantiation
      watcher = FileSystems.getDefault().newWatchService();

      /*
       * A token representing the registration of a watchable object with a
       * WatchService.
       * A watch key is created when a watchable object is registered with a watch
       * service.
       */
      /* storing value not needed if not for reference WatchKey key = */ this.directory.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
                         // needed

      this.startWatching();

    } catch (IOException x) {
      System.err.println(x);
    }
  }


  private void startWatching() {
    for (;;) {
      WatchKey key;
      try {
        key = watcher.take();
      } catch (InterruptedException e) {
        System.out.println("error while taking the watcher");
        // LATER submit this to a new crash event logger
        // System.exit(70); // not treated as a definite return, it is a method
        // invocation and Java compiler does not guarantee this block exits this method
        return;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind kind = event.kind();

        switch (kind.name()) {
          // OVERFLOW = A special event to indicate that events may have been lost or
          // discarded.
          case "ENTRY_DELETE":
          case "OVERFLOW":
            continue;
          // all other events registered still trigger a rescan
          default:
            this.handleEvent(kind.name());
            continue;
        }
      }
      // just avoid OVERFLOW

      // RESET the key to allow Watchkey to pass the new e
      boolean valid = key.reset();
      if (!valid) {
        System.out.println("Directory is no longer accessible");
        break;
      }
    }
  }

  /**
   * Send event to registered listener based on the last execution and debounce time if set
   * @param eventName
   */
  private void handleEvent(String eventName) {
    long timeStamp = 0;
    long timeDiff = 0;

    if (this.lastExecutionTimestamp != 0) {
      timeStamp = Instant.now().getEpochSecond();
      timeDiff = timeStamp - lastExecutionTimestamp;
      
      if (timeDiff < AppConfig.getInt(ConfigKey.DEBOUNCE_TIME)) { 
        return;
      }
    }
    timeStamp = Instant.now().getEpochSecond();

    timeDiff = timeStamp - lastExecutionTimestamp;

    this.lastExecutionTimestamp = Instant.now().getEpochSecond();
    this.notifyChange(eventName + " time difference seconds " + (timeDiff));
  }

  private void notifyChange(String eventName) { // might drop the path, since is just 1
    for (DirectoryEventListener listener : this.listeners) {
      listener.onContentChanged(eventName);
    }
  }

}