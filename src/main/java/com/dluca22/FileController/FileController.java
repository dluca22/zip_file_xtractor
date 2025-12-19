package com.dluca22.FileController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
// import java.util.zip.ZipOutputStream;

import com.dluca22.AppConfig.AppConfig;
import com.dluca22.AppConfig.ConfigKey;
import com.dluca22.DirectoryWatcher.DirectoryEventListener;
import com.dluca22.Tools.FileValidator;
import com.dluca22.Tools.ResultFormatter;

// file controller to scan and analyze files in the given directory
public class FileController implements DirectoryEventListener {

  String directoryPath;
  File directory;
  String customDestDirectory;
  boolean busy = false;

  ArrayList<File> zipFilesInDirectory = new ArrayList<>();
  ArrayList<String> zipFilesNamesInDirectory = new ArrayList<>();

  public FileController(String scanDirectory) {
    // super();

    this.directoryPath = scanDirectory;
    this.directory = new File(this.directoryPath);
    if (AppConfig.getString(ConfigKey.TARGET_DIR) != null) {
      this.customDestDirectory = AppConfig.getString(ConfigKey.TARGET_DIR);
    }
  }

  public void run() {
    this.busy = true; // temp add guard to avoid re-running while still processing
    this.scanFilesInFolder();
    this.processZipFiles();
    this.busy = false;
  }

  // list all the files in the folder and appending them to the array of zipFiles
  // if file is a .zip
  // will later add control to check wether file contains .stl files
  private void scanFilesInFolder() {
    for (final File file : this.directory.listFiles()) {

      String fileName = file.getName();
      // if (file.isFile() && fileName.endsWith(".zip")) {
      if (file.isFile() && FileValidator.isZipFile(file)) {
        this.zipFilesInDirectory.add(file);
        this.zipFilesNamesInDirectory.add(fileName);
      }
    }
  }

  private void processZipFiles() {
    java.util.Iterator<File> iterator = zipFilesInDirectory.iterator();
    while (iterator.hasNext()) {
      File file = iterator.next();
      if (this.zipContainsSTL(file) == true) {
        ResultFormatter result = this.extractContentAndMoveZip(file);
        if (result.success()) {
          iterator.remove();
        }
      }
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
    String zipFilePath = file.getPath();
    String fileName = file.getName();
    try {
      // String destDir = file.getParent() + "/" + fileName.replace(".stl",
      // "").replace(" ", "_");
      // String dest = new Path
      String destDir = this.createTargetDirectory(file);

      // try extract the content of the file to a dir within the same dir
      this.unzip(zipFilePath, destDir);
      // move the zip file inside it;
      this.moveFile(zipFilePath, destDir, fileName);

      return ResultFormatter.success("Action done on " + fileName);
    } catch (IOException e) {
      System.out.println("Failed during creation of target dir " + fileName);
      return ResultFormatter.failure();
    } catch (Exception e) {
      return ResultFormatter.failure();
    }
  }

  String createTargetDirectory(File file) throws IOException {
    try {

      String destDirName = file.getName()
          .replace(".zip", "")
          .replace(" ", "_");

      // Better: use Path API
      String destionationParent = this.getDestinationParent(file);
      Path destDir = Paths.get(destionationParent, destDirName);

      // Create directory and parents if missing
      Files.createDirectories(destDir);

      return destDir.toString();
    } catch (IOException exception) {
      System.out.println("Error on targetDirectory");
      // return "";
      throw exception;
    }
  }

  // private void unzip(String source, String destination) {
  // try {
  // ZipFile zipFile = new ZipFile(source);
  // zipFile.extractAll(destination);
  // } catch (ZipException e) {
  // e.printStackTrace();
  // }

  // }
  // Source - https://stackoverflow.com/a
  // Posted by Oliv, modified by community. See post 'Timeline' for change history
  // Retrieved 2025-12-18, License - CC BY-SA 4.0

  public void unzip2(InputStream is, Path targetDir) throws IOException {
    targetDir = targetDir.toAbsolutePath();
    try (ZipInputStream zipIn = new ZipInputStream(is)) {
      for (ZipEntry ze; (ze = zipIn.getNextEntry()) != null;) {
        Path resolvedPath = targetDir.resolve(ze.getName()).normalize();
        if (!resolvedPath.startsWith(targetDir)) {
          // see: https://snyk.io/research/zip-slip-vulnerability
          throw new RuntimeException("Entry with an illegal path: "
              + ze.getName());
        }
        if (ze.isDirectory()) {
          Files.createDirectories(resolvedPath);
        } else {
          Files.createDirectories(resolvedPath.getParent());
          Files.copy(zipIn, resolvedPath);
        }
      }
    }
  }

  private void unzip(String zipFilePath, String destDir) {
    File dir = new File(destDir);
    // create output directory if it doesn't exist
    if (!dir.exists())
      dir.mkdirs();

    FileInputStream fileInputStream;
    // buffer for read and write data to file
    byte[] buffer = new byte[1024];
    try {

      // create a stream to read content sequentally, does not have "selecatble"
      // entries and access to individual files like "ZipFile"
      fileInputStream = new FileInputStream(zipFilePath);
      // ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
      // ZipEntry zipEntry = zipInputStream.getNextEntry();
      // while (zipEntry != null) {
      // String fileName = zipEntry.getName();
      // File newFile = new File(destDir + File.separator + fileName);
      // System.out.println("Unzipping to " + newFile.getAbsolutePath());
      // // create directories for sub directories in zip
      // new File(newFile.getParent()).mkdirs();

      // FileOutputStream fileOutputStream = new FileOutputStream(newFile);
      // int len;
      // // writes to output stream until EOF
      // while ((len = zipInputStream.read(buffer)) > 0) {
      // fileOutputStream.write(buffer, 0, len);
      // }

      // fileOutputStream.close();
      // // close this ZipEntry
      // zipInputStream.closeEntry();
      // zipEntry = zipInputStream.getNextEntry();
      // }

      // // close last ZipEntry
      // zipInputStream.closeEntry();
      // zipInputStream.close();
      // fileInputStream.close();

      this.unzip2(fileInputStream, Paths.get(destDir));
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void moveFile(String sourceFile, String targetFile, String fileName) {
    Path source = Paths.get(sourceFile);
    Path target = Paths.get(targetFile, fileName);

    try {
      // Ensure target directory exists
      Files.createDirectories(target.getParent());

      Path temp = Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
      if (temp != null) {
        System.out.println("File renamed and moved successfully");
      }
    } catch (IOException e) {
      System.out.println("Failed to move the file");
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

  public String getDestinationParent(File file) {
    return this.customDestDirectory != null ? this.customDestDirectory : file.getParent();
  }

  @Override
  public void onContentChanged(String event) {
    if (this.busy == false) {
      this.run();
    }
  }
}
