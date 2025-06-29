package com.converter;

public class ConvertService {

    public void process(String[] args) {
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
                    inputFile = args[i];
                    break;
                case "-o":
                    i++;
                    outputFile = args[i];
                    break;
                default:
                    if (args[i].contains("-") || args[i].contains("--")) {
                        System.err.println("Unknown option: " + args[i]);
                    } else {
                        System.err.println("Unknown command: " + args[i]);
                    }
                    System.exit(1);
            }
        }
        try {
            Converter converter = getConverter(inputFile, outputFile);
            converter.convert(inputFile, outputFile);
            System.out.println("Convert file successfully.");
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            System.out.println("Error when try to convert, input file error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error when try to convert: " + e.getMessage());
        }
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

    private Converter getConverter(String input, String output) {
        if (input == null || output == null) {
            throw new RuntimeException("convert parameter is invalid");
        }
        if (input.contains(".csv") && output.contains(".parquet")) {
            return new CsvToParquet();
        } else if (input.contains(".parquet") && output.contains(".csv")) {
            return new ParquetToCsv();
        }
        throw new RuntimeException("file format are invalid format");
    }
}
