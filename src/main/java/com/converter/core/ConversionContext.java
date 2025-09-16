package com.converter.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Context object containing all information needed for a conversion operation
 */
public class ConversionContext {
    private final String inputPath;
    private final String outputPath;
    private final FileFormat sourceFormat;
    private final FileFormat targetFormat;
    private final Map<String, Object> options;
    
    private ConversionContext(Builder builder) {
        this.inputPath = builder.inputPath;
        this.outputPath = builder.outputPath;
        this.sourceFormat = builder.sourceFormat;
        this.targetFormat = builder.targetFormat;
        this.options = new HashMap<>(builder.options);
    }
    
    public String getInputPath() {
        return inputPath;
    }
    
    public String getOutputPath() {
        return outputPath;
    }
    
    public FileFormat getSourceFormat() {
        return sourceFormat;
    }
    
    public FileFormat getTargetFormat() {
        return targetFormat;
    }
    
    public Map<String, Object> getOptions() {
        return new HashMap<>(options);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getOption(String key, T defaultValue) {
        return (T) options.getOrDefault(key, defaultValue);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String inputPath;
        private String outputPath;
        private FileFormat sourceFormat;
        private FileFormat targetFormat;
        private Map<String, Object> options = new HashMap<>();
        
        public Builder inputPath(String inputPath) {
            this.inputPath = inputPath;
            return this;
        }
        
        public Builder outputPath(String outputPath) {
            this.outputPath = outputPath;
            return this;
        }
        
        public Builder sourceFormat(FileFormat sourceFormat) {
            this.sourceFormat = sourceFormat;
            return this;
        }
        
        public Builder targetFormat(FileFormat targetFormat) {
            this.targetFormat = targetFormat;
            return this;
        }
        
        public Builder option(String key, Object value) {
            this.options.put(key, value);
            return this;
        }
        
        public Builder options(Map<String, Object> options) {
            this.options.putAll(options);
            return this;
        }
        
        public ConversionContext build() {
            if (inputPath == null || outputPath == null) {
                throw new IllegalArgumentException("Input and output paths are required");
            }
            if (sourceFormat == null || targetFormat == null) {
                throw new IllegalArgumentException("Source and target formats are required");
            }
            return new ConversionContext(this);
        }
    }
}