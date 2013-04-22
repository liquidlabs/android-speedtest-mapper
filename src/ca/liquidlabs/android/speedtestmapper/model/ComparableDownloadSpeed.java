
package ca.liquidlabs.android.speedtestmapper.model;

import java.util.Comparator;

/**
 * Compares download speed among 2 records. Used for sorting records.
 */
public class ComparableDownloadSpeed implements Comparator<SpeedTestRecord> {

    @Override
    public int compare(SpeedTestRecord lhs, SpeedTestRecord rhs) {
        return (lhs.getDownload() < rhs.getDownload() ? -1 :
                (lhs.getDownload() == rhs.getDownload() ? 0 : 1));
    }
}
