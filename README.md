# file-converter
A java program to convert files between CSV and Parquet formats.

## 📦 Features

- ✅ Convert `.csv` to `.parquet`
- ✅ Convert `.parquet` to `.csv`
- ✅ Supports both relative and absolute file paths
- ✅ CLI-friendly with argument parsing
- ✅ Help command: `-h` or `--help`
- ✅ Extensible architecture for future formats
- ✅ Comprehensive input validation
- ✅ Robust error handling
- ✅ Configurable conversion options

---

## 🛠️ Build and Run Instructions (with Maven)

### ✅ Prerequisites

- Java 11 or higher installed
- [Maven](https://maven.apache.org/download.cgi) installed and added to `PATH`
- Git (optional, for cloning the repo)

---

### 🧱 Build the Project

From the root directory of the project, run:

```bash
mvn clean package

## 🚀 Usage

java -jar <jar-file> -i <input-file> -o <output-file>
## Example:
java -jar my-converter-1.0.jar -i <input-file> -o <output-file>
java -jar target/my-converter-1.0.jar -i <input-file> -o <output-file>

## Helper:
java -jar target/my-converter-1.0.jar -h
java -jar target/my-converter-1.0.jar --help

```

### 📦 Components

| Package/Class              | Description                                                              |
|----------------------------|--------------------------------------------------------------------------|
| `Main.java`                | Application entry point                                                 |
| `ConvertService.java`      | Main service orchestrator with CLI argument parsing                     |
| **Core Package**           |                                                                          |
| `FileFormat.java`          | Enumeration of supported file formats with detection logic              |
| `FileConverter.java`       | Main converter interface with metadata support                          |
| `ConversionContext.java`   | Context object for conversion operations (Builder pattern)              |
| **Factory Package**        |                                                                          |
| `ConverterFactory.java`    | Factory for creating appropriate converters                             |
| **Registry Package**       |                                                                          |
| `ConverterRegistry.java`   | Registry for managing and discovering converters                        |
| **Formats Package**        |                                                                          |
| `CsvToParquetConverter.java` | CSV to Parquet conversion implementation                               |
| `ParquetToCsvConverter.java` | Parquet to CSV conversion implementation                               |
| **Config Package**         |                                                                          |
| `ServiceInitializer.java`  | Service bootstrap and converter registration                            |
| **Validation Package**     |                                                                          |
| `FileValidator.java`       | Input validation for file paths and formats                            |
| **Exception Package**      |                                                                          |
| `ConversionException.java` | Base exception for conversion operations                                |
| `UnsupportedFormatException.java` | Exception for unsupported file formats                          |

### 🏗️ Architecture

The application now uses a modular, extensible architecture:

- **Factory Pattern**: Centralized converter creation
- **Registry Pattern**: Dynamic converter registration and discovery  
- **Builder Pattern**: Flexible conversion context construction
- **Strategy Pattern**: Pluggable conversion algorithms

For detailed architecture documentation, see [ARCHITECTURE.md](ARCHITECTURE.md).

Execution flow:
  → Main.java
     → ConvertService.java
        → Parse CLI arguments and validate inputs
        → ServiceInitializer.initialize()
        → FileValidator.validatePaths()
        → ConverterFactory.getConverter()
        → FileConverter.convert()
        → Print success or error message

## 📚 Dependencies

This project uses Java 11 with the following libraries:

| Library                     | Description                                                                 |
|-----------------------------|-----------------------------------------------------------------------------|
| **Apache Parquet**          | Used for reading and writing Parquet files. Efficient columnar storage.     |
| **Apache Avro**             | Defines schema and works with Parquet for data serialization.               |
| **Open CSV**                | Lightweight CSV reader/writer used for parsing and generating CSV files.    |
| **Hadoop Common**           | Required for I/O and filesystem support when working with Parquet.          |
| **Hadoop Client Api**       | Provides APIs for integrates with the Hadoop I/O system.                    |

