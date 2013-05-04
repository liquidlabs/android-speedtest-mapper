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
package ca.liquidlabs.android.speedtestvisualizer.model;

/**
 * List of possible connection type values. These values can be used to show
 * statistics or filter content.
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
     * Used to determine if cellular network was used. Intended to use for
     * different UI representation.
     * 
     * @return {@code true} when cellular network was used,
     *         {@code false otherwise}
     */
    public boolean isCell() {
        return (this != Wifi) && (this != Unknown);
    }

    /**
     * Check if network type is Wifi. Intended to use for different UI
     * representation in maps.
     * 
     * @return {@code true} when wifi network was used, {@code false otherwise}
     */
    public boolean isWifi() {
        return this == Wifi;
    }

    /**
     * Helper method to get enum value from string
     * 
     * @param type Connection type
     * @return equivalent {@link ConnectionType} value if found, otherwise
     *         returns {@link #Unknown}
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
