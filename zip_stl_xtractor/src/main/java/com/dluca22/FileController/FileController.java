package com.dluca22.FileController;

import java.io.File;
import java.util.ArrayList;

// file controller to scan and analyze files in the given directory
public class FileController {

  String scanDirectory;
  ArrayList<String> zipFilesInDirectory = new ArrayList<>();


  public FileController(String scanDirectory) {
    // super();
    this.scanDirectory = scanDirectory;

  }

  // initialize by getting the folder content
  public void init() {
    final File folder = new File(this.scanDirectory);
    listFilesForFolder(folder);

    for( String fileName : zipFilesInDirectory ){

      System.out.println(fileName);
    }
  }

  // list all the files in the folder and appending them to the array of zipFiles if file is a .zip 
  // will later add control to check wether file contains .stl files
  public void listFilesForFolder(final File folder) {
    for (final File fileEntry : folder.listFiles()) {
      // if ( fileEntry.isFile() == false ) {       //fileEntry.isDirectory()) {
      //   // listFilesForFolder(fileEntry); // not recursive, avoid looking for nested zips
      //   continue;
      // } else {
      //   // String[] fileNameParts = fileName.split("\\.", 0);
      //   String fileName = fileEntry.getName();
      //   if(fileName.endsWith(".zip") == false){
      //     continue;
      //   }

      //   this.zipFilesInDirectory.add(fileName);

      // }

      String fileName = fileEntry.getName();

      if(fileEntry.isFile() && fileName.endsWith(".zip")){
        this.zipFilesInDirectory.add(fileName);
      }
    }
  }

}
