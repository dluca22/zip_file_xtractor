package com.dluca22.Main;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.dluca22.AppConfig.AppConfig;
import com.dluca22.AppConfig.ConfigKey;
import com.dluca22.DirectoryWatcher.DirectoryWatcher;
import com.dluca22.FileController.FileController;
import com.dluca22.MioLogger.MioLogger;

public class App {
    public static void main(String[] args) {

        FileController fileController = new FileController(AppConfig.getString(ConfigKey.WATCH_DIR));
        fileController.run();

        MioLogger ilLogger = new MioLogger();

        Path watchDir = Paths.get(AppConfig.getString(ConfigKey.WATCH_DIR));
        DirectoryWatcher dirWatcher = new DirectoryWatcher(watchDir);
        dirWatcher.AddEventListner(ilLogger);
        dirWatcher.AddEventListner(fileController);
        
        dirWatcher.init();
    }
}