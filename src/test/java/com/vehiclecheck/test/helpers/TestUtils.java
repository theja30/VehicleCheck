package com.vehiclecheck.test.helpers;

import org.apache.commons.lang3.StringUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
public class TestUtils {


    public static List<Map<String, String>> readCSVFileFromPath(String path) throws IOException, CsvValidationException {
        List<Map<String, String>> file = new ArrayList<>();
        CSVReader csvReader = new CSVReader(new FileReader(path));
        List<String> headings = Arrays.stream(csvReader.readNext()).map(head -> head.replaceAll("[^a-zA-Z0-9]", "")).toList();
        String[] values = null;
        while ((values = csvReader.readNext()) != null)
        {
            Map<String, String> record = new HashMap<>();
            for (int i = 0; i < headings.size(); i++) {
                try {
                    record.put(headings.get(i), values[i]);
                } catch (IndexOutOfBoundsException e) {
                    record.put(headings.get(i), StringUtils.EMPTY);
                }
            }
            file.add(record);
        }
        return file;
    }

    public static Set<String> fetchSetOfPatternMatchesFromFile(String vehicleRegistrationNumberPattern, String inputFileName) throws IOException {
        Set<String> matchedRecords = new HashSet<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader( inputFileName));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Pattern pattern = Pattern.compile(vehicleRegistrationNumberPattern);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                matchedRecords.add(matcher.group().replaceAll("\\s+", ""));
            }
        }
        return matchedRecords;
    }
}
