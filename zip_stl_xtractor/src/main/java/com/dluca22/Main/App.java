package com.dluca22.Main;

// import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
// import java.util.Arrays;

import com.dluca22.DirectoryWatcher.DirectoryWatcher;
// import com.dluca22.FileController.FileController;
import com.dluca22.MioLogger.MioLogger;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        // FileController fileController = new FileController("/workspace/testFiles");
        // fileController.init();

        MioLogger ilLogger = new MioLogger();

        // final File folder = new File("/workspace/testFiles");
        // scanFilesInFolder(folder);
        Path watchDir = Paths.get("/workspace/testFiles");
        DirectoryWatcher dirWatcher = new DirectoryWatcher(watchDir);
        dirWatcher.AddEventListner(ilLogger);
        dirWatcher.init();

        // dirWatcher.AddEventListner(fileController);
    }
}