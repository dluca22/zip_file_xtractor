package com.dluca22.FileController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
// import java.util.zip.ZipOutputStream;

import com.dluca22.AppConfig.AppConfig;
import com.dluca22.AppConfig.ConfigKey;
import com.dluca22.DirectoryWatcher.DirectoryEventListener;
import com.dluca22.Tools.FileValidator;
import com.dluca22.Tools.ResultFormatter;
import com.dluca22.Tools.Utils;

// file controller to scan and analyze files in the given directory
public class FileController implements DirectoryEventListener {

  Path sourceDir;
  Path destDir;
  boolean isRunning = false;

  // ArrayList<String> zipFilesNamesInDirectory = new ArrayList<>();
  Set<Path> fileList = new HashSet<>();

  public FileController(Path sourcePath) {
    // super();
    System.out.println(String.format("App ln 22: sourcePath is %s", sourcePath));
    this.sourceDir = sourcePath;

    this.setupDestinationDir();
  }

  public void run() {

    if (this.isRunning) {
      return;
    }

    this.isRunning = true;
    this.scanFilesInFolder();
    this.processZipFiles();
    this.isRunning = false;
  }

  // scan the source directory and zip files (matching conditions) to the Set of
  // tracked ones to process
  private void scanFilesInFolder() {

    try (DirectoryStream<Path> dstream = Files.newDirectoryStream(this.sourceDir)) {
      int itemNum = 0;
      for (Path path : dstream) {
        itemNum++;
        if (Files.isDirectory(path)) {
          System.out.println(String.format("[#item %d ] INVALID, DIRECTORY! skipping %s", itemNum, path));
          continue;
        }

        if (FileValidator.isZipFile(path) == false) {
          System.out.println(String.format("[#item %d ] INVALID NOT_ZIP, skipping %s", itemNum, path));
          continue;
        }

        int fileSizeLimitMB = AppConfig.getInt(ConfigKey.FILE_SIZE_LIMIT_MB);
        if (FileValidator.exceedsFileDimensionLImit(path, fileSizeLimitMB) == true) {
          System.out.println(String.format("[#item %d ] INVALID SIZE, %s exceed %d Mb, skipping...", itemNum, path, fileSizeLimitMB));
          continue;
        }
        System.out.println(String.format("[#item %d ] VALID zip file, tracking %s", itemNum, path));

        fileList.add(path); // add file to the traacked ones
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // runs through fileList and checks if contains printable
  private void processZipFiles() {
    java.util.Iterator<Path> iterator = fileList.iterator();
    while (iterator.hasNext()) {
      Path filePath = iterator.next();
      if (this.zipContainsPrintable(filePath) == true) {
        ResultFormatter result = this.extractContentAndMoveZip(filePath);
        if (result.success()) { // if successful remove from tracked list
          System.out.println(String.format("Removed %s from tracked files", filePath));
          iterator.remove();
        } else {
          System.out.println(String.format("Failed processing %s", filePath));
        }
      } else {
        System.out.println("removedddddddddddd");
        iterator.remove();
      }
    }
  }

  // opens the zip file and check if content is of type printable (.stl, .3mf)
  private boolean zipContainsPrintable(Path zip) {

    try (ZipFile zipFile = new ZipFile(zip.toFile())) {
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();

        // Check if entry is a directory
        if (!entry.isDirectory() && FileValidator.is3dPrintableFle(entry)) {
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

  // TODO refactor to separate actions and retry on failure
  private ResultFormatter extractContentAndMoveZip(Path zipFilePath) {
    String zipFileName = zipFilePath.getFileName().toString();

    try {

      Path destDir = this.createTargetDirectory(zipFileName);
      Path absoluteDestDir = destDir.toAbsolutePath().normalize();

      // try extract the content of the file to a dir within the same dir
      this.unzip(zipFilePath, absoluteDestDir);
      // move the zip file inside it;
      this.moveFileZipFile(zipFilePath, absoluteDestDir);

      return ResultFormatter.success("Action done on " + zipFileName);

    } catch (IOException e) {
      System.out.println("Failed during creation of target dir " + e.getMessage()); // TODO move to logger service
      return ResultFormatter.failure();
    }
  }

  // Input for zipFileName, creates the end directory removing .zip and replacing
  // whitespace with _
  // creates the destination path if not existant
  Path createTargetDirectory(String zipFileName) throws IOException {
    // creates the final folder name (replace space with _ and remove the .zip to
    // create a dir)
    String destDirName = zipFileName
        .replace(".zip", "")
        .replace(" ", "_");

    try {
      Path destFile = Paths.get(this.destDir.getFileName().toString(), destDirName); // joins the destionation dir to
                                                                                     // the new fileName (like
                                                                                     // path.resolve)

      // Create directory and parents if missing
      Files.createDirectories(destFile);

      return destFile;
    } catch (IOException exception) {
      throw new IOException(String.format("Error on creating targetDirectory %s", destDirName));
    }
  }

  public void unzip(Path zipFilePath, Path absoluteDestDir) throws IOException {
    

    // targetDir = targetDir.toAbsolutePath();
    try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(zipFilePath))) {

      ZipEntry entryInZipFile;
      while ((entryInZipFile = zipIn.getNextEntry()) != null) {

        // creates a resolved destiination path for each extracted file
        // targetDir = /app/output
        // entry = models/cube.stl
        // resolved = /app/output/models/cube.stl

        Path resolvedPath = absoluteDestDir.resolve(entryInZipFile.getName()).normalize();
        if (!resolvedPath.startsWith(absoluteDestDir)) {

          throw new RuntimeException("Entry with an illegal path: "
              + entryInZipFile.getName());
        }

        // if zipEntry is a dir safe try to create the resolved path (i.e.
        // /app/output/models/ )
        if (entryInZipFile.isDirectory()) {
          Files.createDirectories(resolvedPath);
        } else { // else safe create parent directories to store the zipEntry uncompressed with
                 // REPLACE
          Files.createDirectories(resolvedPath.getParent());
          Files.copy(zipIn, resolvedPath, StandardCopyOption.REPLACE_EXISTING);
        }
      }
    } catch (IOException e) {
      throw new IOException(String.format("Failed to unzip %s into %s", zipFilePath, destDir), e);
    }
  }

  private void moveFileZipFile(Path zipFilePath, Path absoluteDestDir) throws IOException {

    try {
      // Ensure target directory exists
      if (!Files.exists(absoluteDestDir)) {
        throw new IOException("Somehow destDirectory does not exist!!!");
      }
      Path destionationZipFilePath = absoluteDestDir.resolve(zipFilePath.getFileName());

      Path temp = Files.move(zipFilePath, destionationZipFilePath, StandardCopyOption.REPLACE_EXISTING);
      if (temp != null) {
        System.out.println(String.format("Moved zipFile %s into %s", zipFilePath.getFileName().toString(), absoluteDestDir));
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
      throw new IOException(String.format("Failed to move the file: %s to %s", zipFilePath.getFileName().toString(),
          absoluteDestDir.toAbsolutePath()), e);
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

  private void setupDestinationDir() {
    // start by assigning the same path to avoid repeating
    this.destDir = this.sourceDir;
    // get from config
    String destinationString = AppConfig.getString(ConfigKey.TARGET_DIR);

    // not configured, assign same as source
    if (destinationString == null) {
      return;
    }

    try {
      Path desPath = Paths.get(destinationString);
      if (Utils.isSameDirectory(this.sourceDir, desPath) == false) {
        this.destDir = desPath;
      }

    } catch (IOException e) {
      System.out.println("Cannot get required Destination, output redirected to Source dest");
      return; // return with same default source
    }
    ;
  }

  @Override
  public void onContentChanged(String event) {
    if (this.isRunning == false) {
      this.run();
    }
  }
}
