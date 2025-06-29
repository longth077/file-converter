# file-converter
A java program to convert files between CSV and Parquet formats.

## 📦 Features

- ✅ Convert `.csv` to `.parquet`
- ✅ Convert `.parquet` to `.csv`
- ✅ Supports both relative and absolute file paths
- ✅ CLI-friendly with argument parsing
- ✅ Help command: `-h` or `--help`

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

```bash
java -jar <jar-file> -i <input-file> -o <output-file>
Example:
java -jar my-converter-1.0.jar -i <input-file> -o <output-file>
java -jar target/my-converter-1.0.jar -i <input-file> -o <output-file>

Helper:
java -jar target/my-converter-1.0.jar -h
java -jar target/my-converter-1.0.jar --help

### 📦 Components

| Class/File              | Description                                                              |
|-------------------------|--------------------------------------------------------------------------|
| `ConverterService.java` | Main entry point. Parses CLI arguments, validates them, and runs logic.  |
| `Converter.java`        | Interface that define the convert function.                              |
| `CsvToParquet.java`     | Contains logic to read a CSV file and write it as Parquet format.        |
| `ParquetToCsv.java`     | Contains logic to read a Parquet file and output it as CSV.              |


Execution steps:
  → MyConverter.java
     → Parse CLI arguments
     → Determine file extensions
     → Route to CsvToParquet or ParquetToCsv
     → Perform conversion
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

