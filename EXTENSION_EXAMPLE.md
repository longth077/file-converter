# Extension Example: Adding JSON Support

This example demonstrates how easy it is to extend the file converter to support a new format (JSON) using the improved architecture.

## Step 1: Add the new format to the FileFormat enum

```java
// In FileFormat.java
public enum FileFormat {
    CSV("csv", "Comma Separated Values"),
    PARQUET("parquet", "Apache Parquet"),
    JSON("json", "JavaScript Object Notation"); // New format
    
    // ... rest of the enum remains the same
}
```

## Step 2: Add JSON dependency to pom.xml

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

## Step 3: Implement CSV to JSON converter

```java
package com.converter.formats;

import com.converter.core.ConversionContext;
import com.converter.core.FileConverter;
import com.converter.core.FileFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvToJsonConverter implements FileConverter {
    
    @Override
    public void convert(ConversionContext context) throws Exception {
        String csvPath = context.getInputPath();
        String jsonPath = context.getOutputPath();
        
        // Get formatting option (pretty print)
        boolean prettyPrint = context.getOption("prettyPrint", true);
        
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> records = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] headers = reader.readNext();
            if (headers == null) {
                throw new IllegalArgumentException("CSV file is empty: " + csvPath);
            }
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                Map<String, String> record = new HashMap<>();
                for (int i = 0; i < headers.length && i < line.length; i++) {
                    record.put(headers[i], line[i]);
                }
                records.add(record);
            }
        }
        
        // Write JSON
        try (FileWriter writer = new FileWriter(jsonPath)) {
            if (prettyPrint) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(writer, records);
            } else {
                mapper.writeValue(writer, records);
            }
        }
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

## Step 4: Implement JSON to CSV converter

```java
package com.converter.formats;

import com.converter.core.ConversionContext;
import com.converter.core.FileConverter;
import com.converter.core.FileFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class JsonToCsvConverter implements FileConverter {
    
    @Override
    public void convert(ConversionContext context) throws Exception {
        String jsonPath = context.getInputPath();
        String csvPath = context.getOutputPath();
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new FileReader(jsonPath));
        
        if (!rootNode.isArray()) {
            throw new IllegalArgumentException("JSON must be an array of objects");
        }
        
        // Collect all possible field names
        Set<String> allFields = new LinkedHashSet<>();
        for (JsonNode node : rootNode) {
            if (node.isObject()) {
                node.fieldNames().forEachRemaining(allFields::add);
            }
        }
        
        String[] headers = allFields.toArray(new String[0]);
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
            // Write header
            writer.writeNext(headers);
            
            // Write data rows
            for (JsonNode node : rootNode) {
                String[] row = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    JsonNode fieldNode = node.get(headers[i]);
                    row[i] = (fieldNode != null && !fieldNode.isNull()) 
                           ? fieldNode.asText() 
                           : "";
                }
                writer.writeNext(row);
            }
        }
    }
    
    @Override
    public FileFormat getSourceFormat() {
        return FileFormat.JSON;
    }
    
    @Override
    public FileFormat getTargetFormat() {
        return FileFormat.CSV;
    }
}
```

## Step 5: Register the new converters

```java
// In ServiceInitializer.java
public static synchronized void initialize() {
    if (initialized) {
        return;
    }
    
    ConverterRegistry registry = ConverterRegistry.getInstance();
    
    // Register existing converters
    registry.registerConverter(new CsvToParquetConverter());
    registry.registerConverter(new ParquetToCsvConverter());
    
    // Register new JSON converters
    registry.registerConverter(new CsvToJsonConverter());
    registry.registerConverter(new JsonToCsvConverter());
    
    initialized = true;
}
```

## Step 6: Test the new functionality

```bash
# Convert CSV to JSON
java -jar my-converter-1.0.jar -i data.csv -o data.json

# Convert JSON to CSV  
java -jar my-converter-1.0.jar -i data.json -o output.csv
```

## Benefits Demonstrated

1. **Zero changes to existing code**: No modification of Main.java, ConvertService.java, or any existing converters
2. **Automatic integration**: New converters are automatically available through the factory and registry
3. **Consistent interface**: Same CLI commands work with new formats
4. **Configuration support**: New converters can use the option system for customization
5. **Error handling**: Inherits all validation and error handling from the framework

This example shows how the improved architecture makes adding new formats a simple, isolated task that doesn't affect existing functionality.