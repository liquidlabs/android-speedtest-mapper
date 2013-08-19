package ca.liquidlabs.android.speedtestvisualizer.model;

import com.jjoe64.graphview.GraphViewDataInterface;


public class StDataLatencyDate extends SpeedTestRecord implements GraphViewDataInterface {

    /**
     * Constructor for SpeedTestRecord.
     * 
     * @param csvRecord
     */
    public StDataLatencyDate(SpeedTestRecord stRecord) {
        super(stRecord);
    }

    /**
     * Returns data for X-axis.
     */
    @Override
    public double getX() {
        return super.getUpload();
    }

    /**
     * Returns data for Y-axis.
     */
    @Override
    public double getY() {
        return super.getLatency();
    }

}
