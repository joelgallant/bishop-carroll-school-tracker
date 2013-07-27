package org.carroll.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

/**
 * Class used to perform actions on files. Able to delete, copy and read/write
 * text files. (With {@link TextFiles})
 *
 * @author Joel Gallant
 */
public class Files {

    /**
     * Deletes the directory and everything within it.
     *
     * @param file directory
     */
    public static void deleteAllInDirectory(File file) {
        Logging.log("Deleting all in " + file.getPath());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File filex : files) {
                if (filex.isDirectory()) {
                    deleteAllInDirectory(filex);
                } else if (filex.isFile()) {
                    filex.delete();
                }
            }
        }
        file.delete();
    }

    /**
     * Creates the file and all necessary parent folders.
     *
     * @param file file to create
     * @throws IOException thrown when creating the file cannot be completed
     */
    public static void createNewFileWithParents(File file) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    /**
     * Class used to interact with text files.
     */
    public static class TextFiles extends Files {

        /**
         * Returns a string representation of all the contents inside of a file.
         *
         * @param file file to get string from
         * @return contents of the file
         * @throws FileNotFoundException thrown when file does not exist
         */
        public static String getStringFromFile(File file) throws FileNotFoundException {
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            try {
                return new Scanner(file).useDelimiter("\\A").next();
            } catch (java.util.NoSuchElementException e) {
                return "";
            }
        }

        /**
         * Returns a string representation of all the contents inside of an
         * {@link InputStream}.
         *
         * @param file input stream with text contents
         * @return contents of input stream
         */
        public static String getStringFromFile(InputStream file) {
            try {
                return new Scanner(file).useDelimiter("\\A").next();
            } catch (java.util.NoSuchElementException | NullPointerException e) {
                return "";
            }
        }

        /**
         * Sets the contents of a text file to the string.
         *
         * @param file file to set
         * @param text string to write to file
         * @throws IOException thrown when a problem writing occurs
         */
        public static void setStringAsFile(File file, String text) throws IOException {
            if (!file.exists()) {
                Files.createNewFileWithParents(file);
            }
            if (!file.canWrite()) {
                throw new IOException("File cannot be written: " + file);
            }
            try (Writer output = new BufferedWriter(new FileWriter(file))) {
                output.write(text);
            }
        }

        /**
         * Sets the contents of an {@link OutputStream} to the string.
         *
         * @param file output stream to set
         * @param output string to set as contents
         * @throws IOException thrown when a problem writing occurs
         */
        public static void setStringAsFile(OutputStream file, String output) throws IOException {
            try (Writer writer = new OutputStreamWriter(file)) {
                writer.write(output);
            }
        }
    }
}
