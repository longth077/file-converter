package com.converter.config;

import com.converter.formats.CsvToParquetConverter;
import com.converter.formats.ParquetToCsvConverter;
import com.converter.registry.ConverterRegistry;

/**
 * Service initializer that registers all available converters
 */
public class ServiceInitializer {
    private static boolean initialized = false;
    
    /**
     * Initialize the converter service by registering all available converters
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }
        
        ConverterRegistry registry = ConverterRegistry.getInstance();
        
        // Register built-in converters
        registry.registerConverter(new CsvToParquetConverter());
        registry.registerConverter(new ParquetToCsvConverter());
        
        initialized = true;
    }
    
    /**
     * Check if the service has been initialized
     * @return true if initialized
     */
    public static boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Reset initialization state (mainly for testing)
     */
    public static synchronized void reset() {
        if (initialized) {
            ConverterRegistry.getInstance().clear();
            initialized = false;
        }
    }
}