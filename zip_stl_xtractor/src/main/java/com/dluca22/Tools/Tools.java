package com.dluca22.Tools;

import java.io.File;
import java.util.zip.ZipEntry;

/**
 * A utility class providing file extension checking functionality.
 * This class offers static methods to identify file types based on their extensions,
 * supporting both regular files and zip file entries.
 * 
 * <p>Example usage:
 * <pre>
 * {@code
 * if (Tools.isSTLFile(new File("model.stl"))) {
 *     // Process STL file
 * }
 * }
 * </pre>
 * 
 * @author dluca22
 * @version 1.0
 * @since 2024
 */
public class Tools {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Tools() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Extracts the file extension from a filename string.
     * Handles edge cases such as files without extensions, hidden files (starting with dot),
     * and files ending with a dot.
     *
     * @param fileName the filename to extract extension from
     * @return the file extension in lowercase, or empty string if no valid extension found
     * 
     * @examples
     * <ul>
     * <li>"document.pdf" → "pdf"</li>
     * <li>"archive.tar.gz" → "gz"</li>
     * <li>".hiddenfile" → ""</li>
     * <li>"file." → ""</li>
     * <li>"noextension" → ""</li>
     * </ul>
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
     * This method extracts the extension from the filename and compares it to the expected value.
     *
     * @param fileName the filename to check
     * @param expectedExt the expected extension to match against (case-insensitive)
     * @return {@code true} if the file's extension matches the expected extension, {@code false} otherwise
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
     * @return {@code true} if the file has a ".zip" extension, {@code false} otherwise
     * 
     * @examples
     * <ul>
     * <li>"archive.zip" → true</li>
     * <li>"document.pdf" → false</li>
     * <li>"backup.ZIP" → true (case-insensitive)</li>
     * </ul>
     */
    public static boolean isZipFile(File file) {
        return matchExtension(file.getName(), "zip");
    }

    /**
     * Determines if a file is an STL 3D model file based on its extension.
     * STL (Stereolithography) is a common file format for 3D printing.
     *
     * @param file the File object to check
     * @return {@code true} if the file has a ".stl" extension, {@code false} otherwise
     * 
     * @examples
     * <ul>
     * <li>"model.stl" → true</li>
     * <li>"part.STL" → true (case-insensitive)</li>
     * <li>"image.jpg" → false</li>
     * </ul>
     */
    public static boolean isSTLFile(File file) {
        return matchExtension(file.getName(), "stl");
    }

    /**
     * Determines if a zip entry (file inside a zip archive) is an STL 3D model file based on its extension.
     *
     * @param zipEntry the ZipEntry object representing a file inside a zip archive
     * @return {@code true} if the zip entry has a ".stl" extension, {@code false} otherwise
     * 
     * @examples
     * <ul>
     * <li>"models/part.stl" → true</li>
     * <li>"images/photo.png" → false</li>
     * </ul>
     */
    public static boolean isSTLFile(ZipEntry zipEntry) {
        return matchExtension(zipEntry.getName(), "stl");
    }
}