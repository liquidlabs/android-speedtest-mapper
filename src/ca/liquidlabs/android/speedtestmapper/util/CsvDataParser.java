
package ca.liquidlabs.android.speedtestmapper.util;

import ca.liquidlabs.android.speedtestmapper.model.SpeedTestRecord;

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
     * Header string currently supported (taken from exported CSV)
     */
    private static final String ST_DATA_HEADER = "Date,ConnType,Lat,Lon,Download,Upload,Latency,ServerName,InternalIp,ExternalIp";

    

    public static List<SpeedTestRecord> parseCsvData(String csvData) {
        Reader in = new StringReader(getCsvData(csvData));
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

    private static String getCsvData(String stData) {

        int startPosition = stData.indexOf(ST_DATA_HEADER);
        if (startPosition != -1) {
            // found expected header, return CSV data set
            return stData.substring(startPosition);
        }

        // when not found return empty data
        return "";
    }
}
