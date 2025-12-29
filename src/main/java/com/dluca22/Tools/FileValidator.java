package com.dluca22.Tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;

import com.dluca22.AppConfig.AppConfig;
import com.dluca22.AppConfig.ConfigKey;

/**
 * A utility class providing file extension checking functionality.
 * This class offers static methods to identify file types based on their
 * extensions,
 * supporting both regular files and zip file entries.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * {@code
 * if (Tools.isSTLFile(new File("model.stl"))) {
 *     // Process STL file
 * }
 * }
 * </pre>
 */
public class FileValidator {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FileValidator() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Extracts the file extension from a filename string.
     * Handles edge cases such as files without extensions, hidden files (starting
     * with dot),
     * and files ending with a dot.
     *
     * @param fileName the filename to extract extension from
     * @return the file extension in lowercase, or empty string if no valid
     *         extension found
     * 
     * @examples
     *           <ul>
     *           <li>"document.pdf" → "pdf"</li>
     *           <li>"archive.tar.gz" → "gz"</li>
     *           <li>".hiddenfile" → ""</li>
     *           <li>"file." → ""</li>
     *           <li>"noextension" → ""</li>
     *           </ul>
     */
    private static String getFileExtension(String fileName) {
        int lastDotIdx = fileName.lastIndexOf('.');

        if (lastDotIdx > 0 && lastDotIdx < fileName.length() - 1) {
            return fileName.substring(lastDotIdx + 1).toLowerCase();
        }
        return "";
    }

    /**
     * Compares a file's extension with an expected extension, case-insensitively.
     * This method extracts the extension from the filename and compares it to the
     * expected value.
     *
     * @param fileName    the filename to check
     * @param expectedExt the expected extension to match against (case-insensitive)
     * @return {@code true} if the file's extension matches the expected extension,
     *         {@code false} otherwise
     * 
     * @see #getFileExtension(String)
     */
    private static boolean matchExtension(String fileName, String expectedExt) {
        String fileExtension = getFileExtension(fileName);
        return fileExtension.toLowerCase().equals(expectedExt.toLowerCase());
    }

    /**
     * Determines if a file is a ZIP archive based on its extension.
     *
     * @param file the File object to check
     * @return {@code true} if the file has a ".zip" extension, {@code false}
     *         otherwise
     * 
     * @examples
     *           <ul>
     *           <li>"archive.zip" → true</li>
     *           <li>"document.pdf" → false</li>
     *           <li>"backup.ZIP" → true (case-insensitive)</li>
     *           </ul>
     */
    public static boolean isZipFile(File file) {
        return matchExtension(file.getName(), "zip");
    }

    /**
     * Same as {@link #isZipFile(File)}, but operates on a {@link Path}
     * 
     * @param path the file entry to check
     * @return {@code true} if the entry name has a {@code .zip} extension;
     *         {@code false} otherwise
     */
    public static boolean isZipFile(Path file) {
        return matchExtension(file.getFileName().toString(), "zip");
    }

    /**
     * Determines if a file is an STL 3D model file based on its extension.
     * STL (Stereolithography) is a common file format for 3D printing.
     *
     * @param file the File object to check
     * @return {@code true} if the file has a ".stl" extension, {@code false}
     *         otherwise
     * 
     * @examples
     *           <ul>
     *           <li>"model.stl" → true</li>
     *           <li>"part.STL" → true (case-insensitive)</li>
     *           <li>"image.jpg" → false</li>
     *           </ul>
     */
    public static boolean isSTLFile(File file) {
        return matchExtension(file.getName(), "stl");
    }

    /**
     * Same as {@link #isSTLFile(File)}, but operates on a {@link ZipEntry}
     * representing a file inside a ZIP archive.
     *
     * @param zipEntry the ZIP entry to check
     * @return {@code true} if the entry name has a {@code .stl} extension;
     *         {@code false} otherwise
     */
    public static boolean isSTLFile(ZipEntry zipEntry) {
        return matchExtension(zipEntry.getName(), "stl");
    }

    /**
     * Same as {@link #isSTLFile(File)}, but operates on a {@link Path}.
     *
     * @param path the path to check
     * @return {@code true} if the path has a {@code .stl} extension; {@code false}
     *         otherwise
     */
    public static boolean isSTLFile(Path path) {
        return matchExtension(path.getFileName().toString(), "stl");
    }

    /**
     * Determines if a zip entry (file inside a zip archive) is an STL 3D model file
     * based on its extension.
     *
     * @param zipEntry the ZipEntry object representing a file inside a zip archive
     * @return {@code true} if the zip entry has a ".stl" extension, {@code false}
     *         otherwise
     * 
     * @examples
     *           <ul>
     *           <li>"models/part.3mf" → true</li>
     *           <li>"images/photo.png" → false</li>
     *           </ul>
     */
    public static boolean is3MfFile(ZipEntry zipEntry) {
        return matchExtension(zipEntry.getName(), "3mf");
    }

    /**
     * Same as {@link #is3MfFile(ZipEntry zipEntry)}, but operates on a {@link File}
     *
     * @param file the file to check
     * @return {@code true} if the entry name has a {@code .3mf} extension;
     *         {@code false} otherwise
     */
    public static boolean is3MfFile(File file) {
        return matchExtension(file.getName(), "3mf");
    }

    /**
     * Same as {@link #is3MfFile(ZipEntry zipEntry)}, but operates on a {@link Path}
     *
     * @param path the file entity to check
     * @return {@code true} if the entry name has a {@code .3mf} extension;
     *         {@code false} otherwise
     */
    public static boolean is3MfFile(Path path) {
        return matchExtension(path.getFileName().toString(), "3mf");
    }

    /**
     * Determines if a zip entry (file inside a zip archive) is an STL 3D model file
     * based on its extension.
     *
     * @param zipEntry the ZipEntry object representing a file inside a zip archive
     * @return {@code true} if the zip entry has a ".obj" extension, {@code false}
     *         otherwise
     * 
     * @examples
     *           <ul>
     *           <li>"models/part.3mf" → true</li>
     *           <li>"images/photo.png" → false</li>
     *           </ul>
     */
    public static boolean isObjFile(ZipEntry zipEntry) {
        return matchExtension(zipEntry.getName(), "obj");
    }

    /**
     * Same as {@link #isObjFile(ZipEntry zipEntry)}, but operates on a {@link File}
     *
     * @param file the file to check
     * @return {@code true} if the entry name has a {@code .obj} extension;
     *         {@code false} otherwise
     */
    public static boolean isObjFile(File file) {
        return matchExtension(file.getName(), "obj");
    }

    /**
     * Same as {@link #isObjFile(ZipEntry zipEntry)}, but operates on a {@link Path}
     *
     * @param path the file entity to check
     * @return {@code true} if the entry name has a {@code .obj} extension;
     *         {@code false} otherwise
     */
    public static boolean isObjFile(Path path) {
        return matchExtension(path.getFileName().toString(), "obj");
    }

    /**
     * Determines if a zip entry (file inside a zip archive) is an STL 3D model file
     * based on its extension.
     *
     * @param zipEntry the ZipEntry object representing a file inside a zip archive
     * @return {@code true} if the zip entry has one of the accepted files extension
     *         between .stl and .3mf
     * 
     * @examples
     *           <ul>
     *           <li>"models/part.3mf" → true</li>
     *           <li>"models/part.stl" → true</li>
     *           <li>"images/photo.png" → false</li>
     *           </ul>
     */
    public static boolean is3dPrintableFle(ZipEntry zipEntry) {
        return isSTLFile(zipEntry) || is3MfFile(zipEntry) || isObjFile(zipEntry);
    }

    public static boolean is3dPrintableFle(Path path) {
        return isSTLFile(path) || is3MfFile(path) || isObjFile(path);
    }

    public static boolean exceedsFileDimensionLImit(Path filePath, int fileSizeLimitMB) throws IOException {
        long fileSizeBytes = Files.size(filePath);
        long limitSizeBytes = Utils.convertToBytes(fileSizeLimitMB, "MB");
        boolean exceeds = fileSizeBytes > limitSizeBytes;
        if (exceeds) {
            double realFileSizeMB = Utils.convertFromBytes(fileSizeBytes, "MB");
            String sizeExceedingMsg = filePath.getFileName().toString() + " is " + realFileSizeMB
                    + " MB and exceeds the current " + AppConfig.getInt(ConfigKey.FILE_SIZE_LIMIT_MB) + " MB limit";
            // Logger.debug(sizeExceedingMsg) // add to logging service
            System.out.println(sizeExceedingMsg);
        }

        return exceeds;
    }

}