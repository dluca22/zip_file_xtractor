package com.dluca22.FileController;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.dluca22.Tools.Tools;

// file controller to scan and analyze files in the given directory
public class FileController {

  String scanDirectory;
  ArrayList<File> zipFilesInDirectory = new ArrayList<>();
  ArrayList<String> zipFilesNamesInDirectory = new ArrayList<>();

  public FileController(String scanDirectory) {
    // super();
    this.scanDirectory = scanDirectory;

  }

  // initialize by getting the folder content
  public void init() {
    final File folder = new File(this.scanDirectory);
    this.scanFilesInFolder(folder);

    // for( String fileName : zipFilesInDirectory ){

    // System.out.println(fileName);
    // }
    this.processZipFiles();
  }

  // list all the files in the folder and appending them to the array of zipFiles
  // if file is a .zip
  // will later add control to check wether file contains .stl files
  private void scanFilesInFolder(final File folder) {
    for (final File file : folder.listFiles()) {
      // if ( fileEntry.isFile() == false ) { //fileEntry.isDirectory()) {
      // // listFilesForFolder(fileEntry); // not recursive, avoid looking for nested
      // zips
      // continue;
      // } else {
      // // String[] fileNameParts = fileName.split("\\.", 0);
      // String fileName = fileEntry.getName();
      // if(fileName.endsWith(".zip") == false){
      // continue;
      // }

      // this.zipFilesInDirectory.add(fileName);

      // }

      String fileName = file.getName();
      // if (file.isFile() && fileName.endsWith(".zip")) {
      if (file.isFile() && Tools.isZipFile(file)) {
        this.zipFilesInDirectory.add(file);
        this.zipFilesNamesInDirectory.add(fileName);
      }
    }
  }

  private void processZipFiles() {
    for (final File file : this.zipFilesInDirectory) {
      if (this.zipContainsSTL(file) == false) {
        continue;
      }
    }
  }

  private boolean zipContainsSTL(File zip) {
    // ZipFile zipFile = new ZipFile(fileName){

    // };

    try (ZipFile zipFile = new ZipFile(zip)) {
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();
        
        // Check if entry is a directory
        if (!entry.isDirectory() && Tools.isSTLFile(entry)) {
          try (InputStream inputStream = zipFile.getInputStream(entry)) {
            // Read and process the entry contents using the inputStream
            System.out.println("hee");    
          }
        }
      }

    } catch (Exception e) {
      System.err.println(e);
    }
    return false;
  }
}
