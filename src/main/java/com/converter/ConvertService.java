package com.converter;

import com.converter.config.ServiceInitializer;
import com.converter.core.ConversionContext;
import com.converter.core.FileConverter;
import com.converter.core.FileFormat;
import com.converter.factory.ConverterFactory;
import com.converter.validation.FileValidator;

import java.util.List;

public class ConvertService {

    public void process(String[] args) {
        // Initialize the service
        ServiceInitializer.initialize();
        
        processCommand(args);
    }

    private void processCommand(String[] args) {
        String inputFile = null;
        String outputFile = null;
        
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                case "--help":
                    printHelp();
                    System.exit(0);
                case "-i":
                    i++;
                    if (i >= args.length) {
                        System.err.println("Error: -i option requires a file path");
                        System.exit(1);
                    }
                    inputFile = args[i];
                    break;
                case "-o":
                    i++;
                    if (i >= args.length) {
                        System.err.println("Error: -o option requires a file path");
                        System.exit(1);
                    }
                    outputFile = args[i];
                    break;
                default:
                    if (args[i].startsWith("-")) {
                        System.err.println("Unknown option: " + args[i]);
                    } else {
                        System.err.println("Unknown command: " + args[i]);
                    }
                    System.exit(1);
            }
        }
        
        try {
            performConversion(inputFile, outputFile);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error during conversion: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private void performConversion(String inputFile, String outputFile) throws Exception {
        // Validate input parameters
        if (inputFile == null || outputFile == null) {
            throw new IllegalArgumentException("Both input and output file paths are required");
        }
        
        // Validate file paths
        List<String> validationErrors = FileValidator.validatePaths(inputFile, outputFile);
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + String.join(", ", validationErrors));
        }
        
        // Check if conversion is supported
        ConverterFactory factory = ConverterFactory.getInstance();
        if (!factory.isConversionSupported(inputFile, outputFile)) {
            FileFormat sourceFormat = FileFormat.fromFilename(inputFile);
            FileFormat targetFormat = FileFormat.fromFilename(outputFile);
            throw new IllegalArgumentException(
                String.format("Conversion from %s to %s is not supported", 
                    sourceFormat.getDescription(), targetFormat.getDescription()));
        }
        
        // Get converter and perform conversion
        FileConverter converter = factory.getConverter(inputFile, outputFile);
        
        ConversionContext context = ConversionContext.builder()
                .inputPath(inputFile)
                .outputPath(outputFile)
                .sourceFormat(FileFormat.fromFilename(inputFile))
                .targetFormat(FileFormat.fromFilename(outputFile))
                .build();
        
        converter.convert(context);
        System.out.println("File converted successfully.");
        System.out.println("Input: " + inputFile);
        System.out.println("Output: " + outputFile);
    }

    private void printHelp() {
        System.out.println("Usage:\n" +
                "  java -jar my-converter-1.0.jar -i <input-file> -o <output-file>\n" +
                "\n or \n" +
                "  java -jar target/my-converter-1.0.jar -i <input-file> -o <output-file>\n" +
                "\n" +
                "Description:\n" +
                "  Converts between CSV and Parquet file formats.\n" +
                "\n" +
                "Parameters:\n" +
                "  -i <input-file>     Path to the input file. The file extension (.csv or .parquet) is required.\n" +
                "  -o <output-file>    Path to the output file. The file extension (.csv or .parquet) is required.\n" +
                "\n" +
                "Notes:\n" +
                "  - Using java 11 to run.\n" +
                "  - Both relative and absolute file paths are supported.\n" +
                "  - The input and output formats are inferred from the file extensions.\n" +
                "  - Only .csv and .parquet formats are currently supported.\n" +
                "\n" +
                "Examples:\n" +
                "  java -jar my-converter.jar -i source.csv -o data.parquet\n" +
                "  java -jar my-converter.jar -i source.parquet -o data.csv\n" +
                "\n or \n" +
                "  java -jar target/my-converter.jar -i source.csv -o data.parquet\n" +
                "  java -jar target/my-converter.jar -i source.parquet -o data.csv\n" +
                "\n" +
                "Options:\n" +
                "  -h, --help          Show this help message.");
    }
}
