
package ca.liquidlabs.android.speedtestmapper.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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

    //
    // Individual keys for each header elements
    //
    private static final String KEY_DATE = "Date";
    private static final String KEY_CONNTYPE = "ConnType";
    private static final String KEY_LAT = "Lat";
    private static final String KEY_LON = "Lon";
    private static final String KEY_DOWNL = "Download";
    private static final String KEY_UPL = "Upload";
    private static final String KEY_LATENCY = "Latency";
    private static final String KEY_SERVER = "ServerName";
    private static final String KEY_IPINT = "InternalIp";
    private static final String KEY_IPEXT = "ExternalIp";

    public static List<CSVRecord> parseCsvData(String csvData) {
        Reader in = new StringReader(getCsvData(csvData));
        try {
            CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.toBuilder().withHeader().build());
            List<CSVRecord> list = parser.getRecords();
            return list;
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
