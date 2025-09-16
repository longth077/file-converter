package com.converter;

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

public class CsvToParquet implements Converter {
    public void convert(String csvPath, String parquetPath) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> lines = reader.readAll();

            if (lines.isEmpty()) throw new IllegalArgumentException("CSV is empty");

            String[] header = lines.get(0);
            Schema schema = createSchema(header);

            try (ParquetWriter<GenericRecord> writer = AvroParquetWriter.<GenericRecord>builder(
                            new org.apache.hadoop.fs.Path(Paths.get(parquetPath).toUri()))
                    .withSchema(schema)
                    .withCompressionCodec(CompressionCodecName.GZIP)
                    .withConf(new org.apache.hadoop.conf.Configuration()) // Required internally
                    .build()) {

                for (int i = 1; i < lines.size(); i++) {
                    String[] row = lines.get(i);
                    GenericRecord record = new GenericData.Record(schema);
                    for (int j = 0; j < header.length; j++) {
                        record.put(header[j], row[j]);
                    }
                    writer.write(record);
                }
            }
        }
    }

    private static Schema createSchema(String[] headers) {
        StringBuilder schemaStr = new StringBuilder("{\"type\":\"record\",\"name\":\"CsvRecord\",\"fields\":[");
        for (int i = 0; i < headers.length; i++) {
            schemaStr.append("{\"name\":\"")
                    .append(headers[i])
                    .append("\",\"type\":\"string\"}");
            if (i < headers.length - 1) schemaStr.append(",");
        }
        schemaStr.append("]}");
        return new Schema.Parser().parse(schemaStr.toString());
    }
}
