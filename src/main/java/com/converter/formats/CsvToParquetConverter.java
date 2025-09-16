package com.converter.formats;

import com.converter.core.ConversionContext;
import com.converter.core.FileConverter;
import com.converter.core.FileFormat;
import com.opencsv.CSVReader;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.ParquetWriter;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.List;

/**
 * Converter from CSV to Parquet format
 */
public class CsvToParquetConverter implements FileConverter {
    
    @Override
    public void convert(ConversionContext context) throws Exception {
        String csvPath = context.getInputPath();
        String parquetPath = context.getOutputPath();
        
        // Get compression option from context (default to GZIP)
        CompressionCodecName compressionCodec = context.getOption("compression", CompressionCodecName.GZIP);
        
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> lines = reader.readAll();
            
            if (lines.isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty: " + csvPath);
            }
            
            String[] header = lines.get(0);
            Schema schema = createSchema(header);
            
            try (ParquetWriter<GenericRecord> writer = AvroParquetWriter.<GenericRecord>builder(
                            new org.apache.hadoop.fs.Path(Paths.get(parquetPath).toUri()))
                    .withSchema(schema)
                    .withCompressionCodec(compressionCodec)
                    .withConf(new org.apache.hadoop.conf.Configuration())
                    .build()) {
                
                for (int i = 1; i < lines.size(); i++) {
                    String[] row = lines.get(i);
                    GenericRecord record = new GenericData.Record(schema);
                    
                    // Handle cases where row has fewer columns than header
                    for (int j = 0; j < header.length; j++) {
                        String value = (j < row.length) ? row[j] : "";
                        record.put(header[j], value);
                    }
                    writer.write(record);
                }
            }
        }
    }
    
    @Override
    public FileFormat getSourceFormat() {
        return FileFormat.CSV;
    }
    
    @Override
    public FileFormat getTargetFormat() {
        return FileFormat.PARQUET;
    }
    
    private static Schema createSchema(String[] headers) {
        StringBuilder schemaStr = new StringBuilder("{\"type\":\"record\",\"name\":\"CsvRecord\",\"fields\":[");
        for (int i = 0; i < headers.length; i++) {
            // Sanitize field names to be valid Avro identifiers
            String fieldName = sanitizeFieldName(headers[i]);
            schemaStr.append("{\"name\":\"")
                    .append(fieldName)
                    .append("\",\"type\":\"string\"}");
            if (i < headers.length - 1) {
                schemaStr.append(",");
            }
        }
        schemaStr.append("]}");
        return new Schema.Parser().parse(schemaStr.toString());
    }
    
    private static String sanitizeFieldName(String fieldName) {
        // Replace invalid characters with underscores
        return fieldName.replaceAll("[^a-zA-Z0-9_]", "_")
                       .replaceAll("^[^a-zA-Z_]", "_"); // Ensure it starts with letter or underscore
    }
}