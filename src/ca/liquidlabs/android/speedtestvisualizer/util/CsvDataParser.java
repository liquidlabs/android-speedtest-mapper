/*
 * Copyright 2013 Liquid Labs Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.liquidlabs.android.speedtestvisualizer.util;

import ca.liquidlabs.android.speedtestvisualizer.model.SpeedTestRecord;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvDataParser {

    /**
     * TAG used for LogCat
     */
    private static final String LOG_TAG = CsvDataParser.class.getSimpleName();


    /**
     * Parses CSV data
     * 
     * @param csvHeader Header items for csv records
     * @param csvData
     * @return
     */
    public static List<SpeedTestRecord> parseCsvData(String csvHeader, String csvData) {
        Reader in = new StringReader(getCsvData(csvHeader, csvData));
        try {
            CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.toBuilder().withHeader().build());

            // get the parsed records
            List<CSVRecord> list = parser.getRecords();
            // create a list to convert data into SpeedTestRecord model
            List<SpeedTestRecord> speedTestRecords = new ArrayList<SpeedTestRecord>();
            for (CSVRecord csvRecord : list) {
                speedTestRecords.add(new SpeedTestRecord(csvRecord));
            }

            return speedTestRecords;
        } catch (IOException e) {
            Tracer.error(LOG_TAG, e);
        }
        // when no data, send empty list
        return Collections.emptyList();
    }

    /**
     * Returns on CSV data string. Strips out initial paragraph.
     * 
     * @param csvHeader Header items for csv records
     * @param csvData CSV data imported by user
     * @return CSV data text set.
     */
    private static String getCsvData(String csvHeader, String csvData) {
        if (csvData != null) {
            int startPosition = csvData.lastIndexOf(csvHeader);
            if (startPosition != -1) {
                // found expected header, return CSV data set
                return csvData.substring(startPosition);
            }
        }

        // when not found return empty data
        return "";
    }

    /**
     * Checks if input data is valid as expected.
     * 
     * @param csvHeader Header items for csv records
     * @param csvData CSV data imported by user
     * @return {@code true} when header is available, {@code false} otherwise
     */
    public static boolean isValidCsvData(String csvHeader, String csvData) {
        if (csvData != null && !csvData.equals("")) {
            return (csvData.lastIndexOf(csvHeader) != -1);
        }
        return false;
    }
}
