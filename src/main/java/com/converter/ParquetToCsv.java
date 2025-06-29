package com.converter;

import com.opencsv.CSVWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Set;

public class ParquetToCsv implements Converter{
    public void convert(String parquetPath, String csvPath) throws Exception {
        try (ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(
                        new org.apache.hadoop.fs.Path(Paths.get(parquetPath).toUri()))
                .withConf(new org.apache.hadoop.conf.Configuration())
                .build();
             CSVWriter writer = new CSVWriter(new FileWriter(csvPath))) {

            GenericRecord record = reader.read();
            if (record == null) return;

            Set<String> fields = record.getSchema().getFields().stream()
                    .map(f -> f.name()).collect(java.util.stream.Collectors.toSet());

            writer.writeNext(fields.toArray(new String[0]));

            do {
                GenericRecord finalRecord = record;
                String[] row = fields.stream().map(f -> String.valueOf(finalRecord.get(f))).toArray(String[]::new);
                writer.writeNext(row);
                record = reader.read();
            } while (record != null);
        }
    }
}
