package com.dluca22.Main;

import java.io.FileNotFoundException;
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

        // start the directory watcher, not dependent on any logic
        Path sourcePath = Paths.get(sourceDirectory);

        if(Files.exists(sourcePath) == false || Files.isDirectory(sourcePath) == false){
            System.out.println("[ERROR] " + sourcePath + " does not exist, or not a directory.");
            System.exit(404);
            return;
        }

        DirectoryWatcher dirWatcher = new DirectoryWatcher(sourcePath);
        dirWatcher.AddEventListner(ilLogger);

        try {
            fileController = new FileController(sourcePath);
            fileController.run();
            dirWatcher.AddEventListner(fileController);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        dirWatcher.init();
    }
}