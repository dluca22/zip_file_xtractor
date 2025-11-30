package com.dluca22.FileController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.dluca22.Tools.FileValidator;
import com.dluca22.Tools.ResultFormatter;

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
      if (file.isFile() && FileValidator.isZipFile(file)) {
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
      System.out.println("yeee");
      ResultFormatter result = this.extractContentAndMoveZip(file);
    }
  }

  private boolean zipContainsSTL(File zip) {

    try (ZipFile zipFile = new ZipFile(zip)) {
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();

        // Check if entry is a directory
        if (!entry.isDirectory() && FileValidator.isSTLFile(entry)) {
          try (InputStream inputStream = zipFile.getInputStream(entry)) {
            // Read and process the entry contents using the inputStream
            return true;
          }
        }
      }

    } catch (Exception e) {
      System.err.println(e);
      return false;
    }
    return false;
  }

  private ResultFormatter extractContentAndMoveZip(File file) {
    try {
      byte[] buffer = new byte[1024];

      ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
      ZipEntry zipEntry = zis.getNextEntry();
      String destDir = file.getPath() + file.getName().replace(".stl", "") ;
      while (zipEntry != null) {
        File newFile = newFile(destDir, zipEntry);
        if (zipEntry.isDirectory()) {
          if (!newFile.isDirectory() && !newFile.mkdirs()) {
            throw new IOException("Failed to create directory " + newFile);
          }
        } else {
          // fix for Windows-created archives
          File parent = newFile.getParentFile();
          if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + parent);
          }

          // write file content
          FileOutputStream fos = new FileOutputStream(newFile);
          int len;
          while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
          }
          fos.close();
        }
        zipEntry = zis.getNextEntry();
      }

      zis.closeEntry();
      zis.close();
      // try extract the content of the file to a dir within the same dir

      // move the zip file inside it;

      return ResultFormatter.success("test done");
    } catch (Exception e) {
      return ResultFormatter.failure();
    }
  }

  public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
    File destFile = new File(destinationDir, zipEntry.getName());

    String destDirPath = destinationDir.getCanonicalPath();
    String destFilePath = destFile.getCanonicalPath();

    if (!destFilePath.startsWith(destDirPath + File.separator)) {
      throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
    }

    return destFile;
  }
}
