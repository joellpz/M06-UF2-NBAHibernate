package database;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.sun.istack.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OpenCSV {
    public static List<String[]> readCSV(@NotNull String path) {
        try (Reader reader = Files.newBufferedReader(Path.of(path))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                return csvReader.readAll();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
