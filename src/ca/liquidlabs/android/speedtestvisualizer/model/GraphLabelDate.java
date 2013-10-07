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

import com.jjoe64.graphview.CustomLabelFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Custom Label formatter for Dates in X-AXIS.
 * 
 * @author Hossain Khan
 */
public class GraphLabelDate implements CustomLabelFormatter {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d", Locale.US);

    @Override
    public String formatLabel(double value, boolean isValueX) {
        if (isValueX) {
            Date d = new Date((long) value);
            return dateFormat.format(d);
        }
        return null; // let graphview generate Y-axis label for us
    }

}
