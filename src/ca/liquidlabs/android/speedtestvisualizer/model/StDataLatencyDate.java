
package ca.liquidlabs.android.speedtestvisualizer.model;

import com.jjoe64.graphview.GraphViewDataInterface;

import org.apache.commons.lang3.math.NumberUtils;

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
        return NumberUtils.toDouble(Long.toString(getUnixTimeStamp()));
    }

    /**
     * Returns data for Y-axis.
     */
    @Override
    public double getY() {
        return getLatency();
    }

}
