package com.converter.validation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator for file conversion operations
 */
public class FileValidator {
    
    /**
     * Validate input and output file paths
     * @param inputPath the input file path
     * @param outputPath the output file path
     * @return list of validation errors (empty if valid)
     */
    public static List<String> validatePaths(String inputPath, String outputPath) {
        List<String> errors = new ArrayList<>();
        
        // Validate input path
        if (inputPath == null || inputPath.trim().isEmpty()) {
            errors.add("Input file path cannot be null or empty");
        } else {
            Path inputFilePath = Paths.get(inputPath);
            if (!Files.exists(inputFilePath)) {
                errors.add("Input file does not exist: " + inputPath);
            } else if (!Files.isRegularFile(inputFilePath)) {
                errors.add("Input path is not a regular file: " + inputPath);
            } else if (!Files.isReadable(inputFilePath)) {
                errors.add("Input file is not readable: " + inputPath);
            }
        }
        
        // Validate output path
        if (outputPath == null || outputPath.trim().isEmpty()) {
            errors.add("Output file path cannot be null or empty");
        } else {
            Path outputFilePath = Paths.get(outputPath);
            Path outputDir = outputFilePath.getParent();
            
            // Check if output directory exists and is writable
            if (outputDir != null && Files.exists(outputDir)) {
                if (!Files.isDirectory(outputDir)) {
                    errors.add("Output directory path is not a directory: " + outputDir);
                } else if (!Files.isWritable(outputDir)) {
                    errors.add("Output directory is not writable: " + outputDir);
                }
            }
            
            // Check if output file already exists and is writable
            if (Files.exists(outputFilePath)) {
                if (!Files.isRegularFile(outputFilePath)) {
                    errors.add("Output path exists but is not a regular file: " + outputPath);
                } else if (!Files.isWritable(outputFilePath)) {
                    errors.add("Output file exists but is not writable: " + outputPath);
                }
            }
        }
        
        return errors;
    }
    
    /**
     * Check if a file exists and is readable
     * @param filePath the file path to check
     * @return true if file exists and is readable
     */
    public static boolean isReadableFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
    }
    
    /**
     * Check if a directory is writable
     * @param dirPath the directory path to check
     * @return true if directory is writable
     */
    public static boolean isWritableDirectory(String dirPath) {
        if (dirPath == null || dirPath.trim().isEmpty()) {
            return false;
        }
        
        Path path = Paths.get(dirPath);
        return Files.exists(path) && Files.isDirectory(path) && Files.isWritable(path);
    }
}