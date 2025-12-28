package com.dluca22.Main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.dluca22.AppConfig.AppConfig;
import com.dluca22.AppConfig.ConfigKey;
import com.dluca22.DirectoryWatcher.DirectoryWatcher;
import com.dluca22.FileController.FileController;
import com.dluca22.MioLogger.MioLogger;

public class App {
    public static void main(String[] args) {

        MioLogger ilLogger = new MioLogger(); // test listener
        FileController fileController;
        String sourceDirectory = AppConfig.getString(ConfigKey.WATCH_DIR);
        System.out.println(String.format("App ln 19: sourceDirectory is %s", sourceDirectory));

        // start the directory watcher, not dependent on any logic
        Path sourcePath = Paths.get(sourceDirectory);
        System.out.println(String.format("App ln 22: sourcePath is %s", sourcePath));

        if (Files.exists(sourcePath) == false || Files.isDirectory(sourcePath) == false) {
            System.out.println("[ERROR] " + sourcePath + " does not exist, or not a directory.");
            System.exit(404);
            return;
        }

        DirectoryWatcher dirWatcher = new DirectoryWatcher(sourcePath);
        dirWatcher.AddEventListner(ilLogger);

        fileController = new FileController(sourcePath);
        fileController.run();
        dirWatcher.AddEventListner(fileController);

        dirWatcher.init();
    }
}