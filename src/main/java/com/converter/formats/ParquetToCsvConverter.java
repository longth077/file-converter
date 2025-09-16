package com.converter.formats;

import com.converter.core.ConversionContext;
import com.converter.core.FileConverter;
import com.converter.core.FileFormat;
import com.opencsv.CSVWriter;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Converter from Parquet to CSV format
 */
public class ParquetToCsvConverter implements FileConverter {
    
    @Override
    public void convert(ConversionContext context) throws Exception {
        String parquetPath = context.getInputPath();
        String csvPath = context.getOutputPath();
        
        // Get option for including header (default to true)
        boolean includeHeader = context.getOption("includeHeader", true);
        
        try (ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(
                        new org.apache.hadoop.fs.Path(Paths.get(parquetPath).toUri()))
                .withConf(new org.apache.hadoop.conf.Configuration())
                .build();
             CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {
            
            GenericRecord record = reader.read();
            if (record == null) {
                // Empty parquet file, just create an empty CSV
                return;
            }
            
            // Get field names in a consistent order
            Schema schema = record.getSchema();
            List<Schema.Field> fields = schema.getFields();
            String[] fieldNames = fields.stream()
                    .map(Schema.Field::name)
                    .toArray(String[]::new);
            
            // Write header if requested
            if (includeHeader) {
                writer.writeNext(fieldNames);
            }
            
            // Write data rows
            do {
                String[] row = new String[fieldNames.length];
                for (int i = 0; i < fieldNames.length; i++) {
                    Object value = record.get(fieldNames[i]);
                    row[i] = (value != null) ? value.toString() : "";
                }
                writer.writeNext(row);
                record = reader.read();
            } while (record != null);
        }
    }
    
    @Override
    public FileFormat getSourceFormat() {
        return FileFormat.PARQUET;
    }
    
    @Override
    public FileFormat getTargetFormat() {
        return FileFormat.CSV;
    }
}