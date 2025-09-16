# File Converter - Improved Architecture Documentation

## Overview
This document describes the improved architecture designed for the file-converter project to support future expansion and maintainability.

## Architecture Goals
- **Extensibility**: Easy addition of new file formats
- **Maintainability**: Clean separation of concerns
- **Testability**: Modular design for better testing
- **Configurability**: Support for conversion options
- **Robustness**: Comprehensive error handling and validation

## Package Structure

```
com.converter/
├── core/                   # Core interfaces and domain objects
│   ├── FileFormat.java     # Enum of supported file formats
│   ├── FileConverter.java  # Main converter interface
│   └── ConversionContext.java # Context object for conversion operations
├── factory/                # Factory pattern implementation
│   └── ConverterFactory.java # Factory for creating converters
├── registry/               # Registry pattern implementation
│   └── ConverterRegistry.java # Registry for managing converters
├── formats/                # Format-specific converter implementations
│   ├── CsvToParquetConverter.java
│   └── ParquetToCsvConverter.java
├── config/                 # Configuration and initialization
│   └── ServiceInitializer.java # Service bootstrap and initialization
├── validation/             # Input validation
│   └── FileValidator.java  # File path and format validation
├── exception/              # Custom exceptions
│   ├── ConversionException.java
│   └── UnsupportedFormatException.java
├── legacy/                 # Deprecated classes (backward compatibility)
│   ├── Converter.java
│   ├── CsvToParquet.java
│   └── ParquetToCsv.java
├── Main.java              # Application entry point
└── ConvertService.java    # Main service orchestrator
```

## Key Design Patterns

### 1. Factory Pattern
**Purpose**: Centralized creation of converter instances  
**Implementation**: `ConverterFactory`
- Provides clean API for getting converters
- Handles format detection from file extensions
- Delegates to registry for actual converter lookup

### 2. Registry Pattern
**Purpose**: Dynamic registration and discovery of converters  
**Implementation**: `ConverterRegistry`
- Singleton pattern for global converter registry
- Thread-safe registration and lookup
- Supports runtime addition of new converters

### 3. Builder Pattern
**Purpose**: Flexible construction of conversion contexts  
**Implementation**: `ConversionContext.Builder`
- Clean API for setting conversion parameters
- Support for optional configuration parameters
- Immutable context objects

### 4. Strategy Pattern
**Purpose**: Pluggable conversion algorithms  
**Implementation**: `FileConverter` interface
- Each converter implements specific format conversion
- Easy to add new format combinations
- Consistent interface across all converters

## Core Components

### FileFormat Enum
- Defines all supported file formats
- Provides format detection from file extensions
- Extensible for new formats

### FileConverter Interface
```java
public interface FileConverter {
    void convert(ConversionContext context) throws Exception;
    FileFormat getSourceFormat();
    FileFormat getTargetFormat();
    boolean supports(FileFormat source, FileFormat target);
    String getDescription();
}
```

### ConversionContext
- Immutable context object containing:
  - Input/output file paths
  - Source/target formats
  - Optional conversion parameters
- Built using Builder pattern

## Extension Points

### Adding New File Formats

1. **Add format to enum**:
```java
// In FileFormat.java
JSON("json", "JavaScript Object Notation"),
XML("xml", "Extensible Markup Language")
```

2. **Implement converter**:
```java
public class CsvToJsonConverter implements FileConverter {
    @Override
    public void convert(ConversionContext context) throws Exception {
        // Implementation
    }
    
    @Override
    public FileFormat getSourceFormat() {
        return FileFormat.CSV;
    }
    
    @Override
    public FileFormat getTargetFormat() {
        return FileFormat.JSON;
    }
}
```

3. **Register converter**:
```java
// In ServiceInitializer.java
registry.registerConverter(new CsvToJsonConverter());
```

### Adding Conversion Options

Conversion options can be passed through the ConversionContext:

```java
ConversionContext context = ConversionContext.builder()
    .inputPath("input.csv")
    .outputPath("output.parquet")
    .sourceFormat(FileFormat.CSV)
    .targetFormat(FileFormat.PARQUET)
    .option("compression", CompressionCodecName.SNAPPY)
    .option("includeHeader", true)
    .build();
```

### Adding Validation Rules

New validation rules can be added to `FileValidator`:

```java
public static List<String> validateJsonFile(String filePath) {
    // Custom validation logic
}
```

## Benefits of New Architecture

### 1. Extensibility
- New formats require minimal code changes
- No modification of existing code when adding formats
- Plugin-like architecture for converters

### 2. Maintainability
- Clear separation of concerns
- Single responsibility principle
- Easy to locate and modify specific functionality

### 3. Testability
- Each component can be tested in isolation
- Mock objects can be easily created
- Builder pattern simplifies test data creation

### 4. Error Handling
- Consistent error handling across all converters
- Specific exception types for different error conditions
- Comprehensive input validation

### 5. Configuration
- Flexible option passing through context
- Format-specific configuration support
- Runtime configuration possible

## Migration from Legacy Code

The legacy converter classes are preserved in the `legacy` package for reference:
- `Converter.java` → `FileConverter.java`
- `CsvToParquet.java` → `CsvToParquetConverter.java`
- `ParquetToCsv.java` → `ParquetToCsvConverter.java`

The new implementation maintains full backward compatibility in terms of functionality while providing a much more extensible foundation.

## Usage Examples

### Basic Usage (same as before)
```bash
java -jar my-converter-1.0.jar -i input.csv -o output.parquet
```

### Programmatic Usage (for future API)
```java
// Initialize service
ServiceInitializer.initialize();

// Create conversion context
ConversionContext context = ConversionContext.builder()
    .inputPath("input.csv")
    .outputPath("output.parquet")
    .sourceFormat(FileFormat.CSV)
    .targetFormat(FileFormat.PARQUET)
    .option("compression", CompressionCodecName.GZIP)
    .build();

// Get converter and perform conversion
ConverterFactory factory = ConverterFactory.getInstance();
FileConverter converter = factory.getConverter(
    context.getSourceFormat(), 
    context.getTargetFormat()
);
converter.convert(context);
```

## Future Enhancements

1. **Plugin System**: Load converters from external JARs
2. **Configuration Files**: YAML/JSON configuration for default options
3. **Batch Processing**: Convert multiple files in one operation
4. **Progress Reporting**: Callbacks for conversion progress
5. **Streaming Support**: Handle large files with streaming
6. **Format Validation**: Validate file content before conversion
7. **Logging**: Comprehensive logging framework
8. **REST API**: HTTP API for conversion services
9. **GUI**: Desktop or web interface
10. **Cloud Integration**: Support for cloud storage systems

This architecture provides a solid foundation for all these future enhancements while maintaining the simplicity of the current CLI interface.