package ca.liquidlabs.android.speedtestmapper.model;


/**
 * List of possible connection type values
 */
public enum ConnectionType {
    Cell,
    Wifi,
    Gprs,
    Edge,
    Umts,
    Cdma,
    Evdo0,
    EvdoA,
    OnexRTT,
    Hsdpa,
    Hspa,
    Iden,
    Ehrpd,
    EvdoB,
    Lte,
    Hsupa,
    WiMax,
    Unknown;

    /**
     * Helper method to get enum value from string
     * 
     * @param type Connection type
     * @return equivalent {@link ConnectionType} value if found, else returns
     *         {@link #Unknown}
     */
    public static ConnectionType fromString(String type) {
        if (type != null) {
            try {
                return ConnectionType.valueOf(type);
            } catch (IllegalArgumentException e) {
                // value not in list - return indeterminable
                return Unknown;
            }
        }
        return Unknown;
    }
}
