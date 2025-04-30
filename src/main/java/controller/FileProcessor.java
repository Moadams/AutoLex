package main.java.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class FileProcessor{

    /**
     * Reads the contents of a file at the given path into a string.
     * @param filePath the path of the file to read
     * @return the contents of the file as a string
     * @throws IOException if an error occurs while reading the file
     */
    public String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    /**
     * Writes the given content to a file at the given path, overwriting any existing file if it exists.
     * @param filePath the path of the file to write
     * @param content the content to write to the file
     * @throws IOException if an error occurs while writing the file
     */
    public void writeFile(String filePath, String content) throws IOException {
        Files.writeString(Path.of(filePath), content);
    }

    /**
     * Appends the given content to a file at the given path, appending to any existing file if it exists.
     * @param filePath the path of the file to append to
     * @param content the content to append to the file
     * @throws IOException if an error occurs while appending to the file
     */
    public void appendToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), content.getBytes(), StandardOpenOption.APPEND);
    }

    /**
     * Reads the contents of multiple files and returns their contents as a list of strings.
     *
     * @param filePaths a list of file paths to read
     * @return a list of strings, each containing the contents of a corresponding file
     * @throws IOException if an error occurs while reading any of the files
     */

    public List<String> batchReadMultipleFiles(List<String> filePaths) throws IOException {
        return filePaths.stream().map(
            path->{
                try {
                    return Files.readString(Path.of(path));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        ).collect(Collectors.toList());
    }

    /**
     * Writes the given contents to multiple files at the given paths, overwriting any existing files if they exist.
     * @param filePaths a list of file paths to write
     * @param contents a list of contents to write to the files
     * @throws IOException if an error occurs while writing any of the files
     * @throws RuntimeException if the lists of file paths and contents are not of equal length
     */
    public void batchWriteMultipleFiles(List<String> filePaths, List<String> contents) throws IOException {
        if(filePaths.size() != contents.size()) throw new RuntimeException("Number of file paths and contents must be equal.");

        for (int i = 0; i < filePaths.size(); i++) {
            writeFile(filePaths.get(i), contents.get(i));
        }
    }
}