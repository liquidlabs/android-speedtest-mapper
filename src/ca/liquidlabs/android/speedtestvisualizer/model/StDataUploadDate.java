
package ca.liquidlabs.android.speedtestvisualizer.model;

import com.jjoe64.graphview.GraphViewDataInterface;

public class StDataUploadDate extends SpeedTestRecord implements GraphViewDataInterface {

    /**
     * Constructor for SpeedTestRecord.
     * 
     * @param csvRecord
     */
    public StDataUploadDate(SpeedTestRecord stRecord) {
        super(stRecord);
    }

    /**
     * Returns data for X-axis.
     */
    @Override
    public double getX() {
        return super.getLatency();
    }

    /**
     * Returns data for Y-axis.
     */
    @Override
    public double getY() {
        return super.getUpload();
    }

}
