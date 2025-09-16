package com.converter.registry;

import com.converter.core.FileConverter;
import com.converter.core.FileFormat;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing file converters
 */
public class ConverterRegistry {
    private static final ConverterRegistry INSTANCE = new ConverterRegistry();
    
    private final Map<String, FileConverter> converters = new ConcurrentHashMap<>();
    
    private ConverterRegistry() {
        // Private constructor for singleton
    }
    
    public static ConverterRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Register a converter
     * @param converter the converter to register
     */
    public void registerConverter(FileConverter converter) {
        String key = createKey(converter.getSourceFormat(), converter.getTargetFormat());
        converters.put(key, converter);
    }
    
    /**
     * Get a converter for the specified format conversion
     * @param sourceFormat the source format
     * @param targetFormat the target format
     * @return the converter if found
     * @throws IllegalArgumentException if no converter is found
     */
    public FileConverter getConverter(FileFormat sourceFormat, FileFormat targetFormat) {
        String key = createKey(sourceFormat, targetFormat);
        FileConverter converter = converters.get(key);
        
        if (converter == null) {
            throw new IllegalArgumentException(
                String.format("No converter found for %s to %s", 
                    sourceFormat.name(), targetFormat.name()));
        }
        
        return converter;
    }
    
    /**
     * Check if a conversion is supported
     * @param sourceFormat the source format
     * @param targetFormat the target format
     * @return true if conversion is supported
     */
    public boolean isSupported(FileFormat sourceFormat, FileFormat targetFormat) {
        String key = createKey(sourceFormat, targetFormat);
        return converters.containsKey(key);
    }
    
    /**
     * Get all registered converters
     * @return unmodifiable collection of converters
     */
    public Collection<FileConverter> getAllConverters() {
        return Collections.unmodifiableCollection(converters.values());
    }
    
    /**
     * Get all supported source formats
     * @return set of supported source formats
     */
    public Set<FileFormat> getSupportedSourceFormats() {
        Set<FileFormat> formats = new HashSet<>();
        for (FileConverter converter : converters.values()) {
            formats.add(converter.getSourceFormat());
        }
        return formats;
    }
    
    /**
     * Get all supported target formats
     * @return set of supported target formats
     */
    public Set<FileFormat> getSupportedTargetFormats() {
        Set<FileFormat> formats = new HashSet<>();
        for (FileConverter converter : converters.values()) {
            formats.add(converter.getTargetFormat());
        }
        return formats;
    }
    
    /**
     * Clear all registered converters (mainly for testing)
     */
    public void clear() {
        converters.clear();
    }
    
    private String createKey(FileFormat sourceFormat, FileFormat targetFormat) {
        return sourceFormat.name() + "->" + targetFormat.name();
    }
}