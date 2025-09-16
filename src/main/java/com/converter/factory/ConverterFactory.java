package com.converter.factory;

import com.converter.core.FileConverter;
import com.converter.core.FileFormat;
import com.converter.registry.ConverterRegistry;

/**
 * Factory for creating and retrieving file converters
 */
public class ConverterFactory {
    private static final ConverterFactory INSTANCE = new ConverterFactory();
    private final ConverterRegistry registry;
    
    private ConverterFactory() {
        this.registry = ConverterRegistry.getInstance();
    }
    
    public static ConverterFactory getInstance() {
        return INSTANCE;
    }
    
    /**
     * Get a converter for the specified file paths
     * @param inputPath the input file path
     * @param outputPath the output file path
     * @return the appropriate converter
     * @throws IllegalArgumentException if no suitable converter is found
     */
    public FileConverter getConverter(String inputPath, String outputPath) {
        FileFormat sourceFormat = FileFormat.fromFilename(inputPath);
        FileFormat targetFormat = FileFormat.fromFilename(outputPath);
        
        return registry.getConverter(sourceFormat, targetFormat);
    }
    
    /**
     * Get a converter for the specified formats
     * @param sourceFormat the source format
     * @param targetFormat the target format
     * @return the appropriate converter
     * @throws IllegalArgumentException if no suitable converter is found
     */
    public FileConverter getConverter(FileFormat sourceFormat, FileFormat targetFormat) {
        return registry.getConverter(sourceFormat, targetFormat);
    }
    
    /**
     * Check if conversion is supported for the given file paths
     * @param inputPath the input file path
     * @param outputPath the output file path
     * @return true if conversion is supported
     */
    public boolean isConversionSupported(String inputPath, String outputPath) {
        try {
            FileFormat sourceFormat = FileFormat.fromFilename(inputPath);
            FileFormat targetFormat = FileFormat.fromFilename(outputPath);
            return registry.isSupported(sourceFormat, targetFormat);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Check if conversion is supported for the given formats
     * @param sourceFormat the source format
     * @param targetFormat the target format
     * @return true if conversion is supported
     */
    public boolean isConversionSupported(FileFormat sourceFormat, FileFormat targetFormat) {
        return registry.isSupported(sourceFormat, targetFormat);
    }
}