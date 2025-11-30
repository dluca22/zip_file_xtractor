package com.dluca22.Main;

// import java.io.File;
// import java.util.Arrays;

import com.dluca22.FileController.FileController;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        // final File folder = new File("/workspace/testFiles");
        // scanFilesInFolder(folder);

        FileController fileController = new FileController("/workspace/testFiles");
        fileController.init();
    }

        // public static void listFilesForFolder(final File folder) {
    //     for (final File fileEntry : folder.listFiles()) {
    //         if (fileEntry.isDirectory()) {
    //             listFilesForFolder(fileEntry);
    //         } else {
    //             System.out.println(fileEntry.getName());
    //             String fileName = fileEntry.getName();
    //             System.out.println(fileName.endsWith(".zip"));
    //             String[] fileNameParts = fileName.split("\\.", 0);
                
    //             System.out.println(Arrays.toString(fileNameParts));


    //         }
    //     }
    // }
}