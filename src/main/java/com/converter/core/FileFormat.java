package com.converter.core;

/**
 * Enumeration of supported file formats
 */
public enum FileFormat {
    CSV("csv", "Comma Separated Values"),
    PARQUET("parquet", "Apache Parquet");

    private final String extension;
    private final String description;

    FileFormat(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    public String getExtension() {
        return extension;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Detect file format from file extension
     * @param filename the filename to analyze
     * @return the detected file format
     * @throws IllegalArgumentException if format is not supported
     */
    public static FileFormat fromFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        
        String lowerFilename = filename.toLowerCase();
        for (FileFormat format : values()) {
            if (lowerFilename.endsWith("." + format.extension)) {
                return format;
            }
        }
        
        throw new IllegalArgumentException("Unsupported file format for: " + filename);
    }
}