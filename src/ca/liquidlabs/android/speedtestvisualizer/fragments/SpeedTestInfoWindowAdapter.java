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
package ca.liquidlabs.android.speedtestvisualizer.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.liquidlabs.android.speedtestvisualizer.R;
import ca.liquidlabs.android.speedtestvisualizer.model.ConnectionType;
import ca.liquidlabs.android.speedtestvisualizer.util.AppConstants;
import ca.liquidlabs.android.speedtestvisualizer.util.Tracer;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import org.apache.commons.lang3.StringUtils;

/**
 * Provides views for customized rendering of info-windows.<br/>
 * When constructing an info-window, methods in this class are called in a
 * defined order.
 * 
 * @author Hossain Khan
 */
public class SpeedTestInfoWindowAdapter implements InfoWindowAdapter {

    private static final String LOG_TAG = SpeedTestInfoWindowAdapter.class.getSimpleName();
    private final View mContentsView;

    public SpeedTestInfoWindowAdapter(final LayoutInflater inflater) {
        mContentsView = inflater.inflate(R.layout.speedtest_record_info, null);
    }

    /**
     * Provides custom contents for the default info-window frame of a marker.
     * This method is only called if getInfoWindow(Marker) first returns null.
     * If this method returns a view, it will be placed inside the default
     * info-window frame.
     * 
     * @param marker The marker for which an info window is being populated.
     * @return A custom view to display as contents in the info window for
     *         marker
     */
    @Override
    public View getInfoContents(Marker marker) {
        Tracer.debug(LOG_TAG, "getInfoContents()");
        renderContents(marker, mContentsView);
        return mContentsView;
    }

    /**
     * Provides a custom info-window for a marker. <br/>
     * NOTE: Currently not used - using default window box
     */
    @Override
    public View getInfoWindow(Marker marker) {
        Tracer.debug(LOG_TAG, "getInfoWindow()");
        return null;
    }

    /**
     * Renders marker contents into the infobox
     * 
     * @param marker Map marker
     * @param view Custom infobox view
     */
    private void renderContents(Marker marker, View view) {
        String[] snippetInfo = StringUtils.split(marker.getSnippet(), AppConstants.TEXT_SEPARATOR);
        
        TextView infoHeading = (TextView) view.findViewById(R.id.txt_info_heading);
        infoHeading.setText("@ " + marker.getTitle());

        TextView downloadSpeed = (TextView) view.findViewById(R.id.txt_info_download);
        downloadSpeed.setText(snippetInfo[1]);

        TextView uploadSpeed = (TextView) view.findViewById(R.id.txt_info_upload);
        uploadSpeed.setText(snippetInfo[2]);

        // Connection type - update image based on type
        ConnectionType connType = ConnectionType.fromString(snippetInfo[0]);
        ImageView conntypeImage = (ImageView) view.findViewById(R.id.img_legend_conntype);
        if (connType.isWifi()) {
            conntypeImage.setImageResource(R.drawable.ic_connection_wifi);
        } else {
            conntypeImage.setImageResource(R.drawable.ic_connection_cell);
        }
        TextView connTypeTxt = (TextView) view.findViewById(R.id.txt_info_conntype);
        connTypeTxt.setText(snippetInfo[0]);
    }

}
