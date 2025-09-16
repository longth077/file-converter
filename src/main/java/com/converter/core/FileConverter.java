package com.converter.core;

/**
 * Enhanced converter interface with metadata support
 */
public interface FileConverter {
    
    /**
     * Convert a file from one format to another
     * @param context the conversion context containing input/output paths and options
     * @throws Exception if conversion fails
     */
    void convert(ConversionContext context) throws Exception;
    
    /**
     * Get the source format this converter handles
     * @return the source file format
     */
    FileFormat getSourceFormat();
    
    /**
     * Get the target format this converter produces
     * @return the target file format
     */
    FileFormat getTargetFormat();
    
    /**
     * Check if this converter supports the given conversion
     * @param sourceFormat the source format
     * @param targetFormat the target format
     * @return true if conversion is supported
     */
    default boolean supports(FileFormat sourceFormat, FileFormat targetFormat) {
        return getSourceFormat() == sourceFormat && getTargetFormat() == targetFormat;
    }
    
    /**
     * Get a description of what this converter does
     * @return human-readable description
     */
    default String getDescription() {
        return String.format("Converts %s to %s", 
            getSourceFormat().getDescription(), 
            getTargetFormat().getDescription());
    }
}