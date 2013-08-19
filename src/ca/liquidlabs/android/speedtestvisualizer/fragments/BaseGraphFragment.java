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

import android.app.Activity;
import android.support.v4.app.Fragment;

import ca.liquidlabs.android.speedtestvisualizer.util.SpeedTestRecordProcessorTask.OnDataProcessorListener;

/**
 * Base fragment with common functionalities to draw graph/chart fragment.
 * 
 * @author GamerX
 */
public abstract class BaseGraphFragment extends Fragment implements OnDataProcessorListener {

    /**
     * Shows progress animation in ActioBar.
     */
    protected void showProgressIndicator() {
        final Activity activity = getActivity();
        if (activity.isFinishing()) {
            return;
        }
        activity.setProgressBarIndeterminateVisibility(true);
        activity.setProgressBarIndeterminate(true);
    }

    /**
     * Hides progress animation in ActionBar.
     */
    protected void hideProgressIndicator() {
        final Activity activity = getActivity();
        if (activity.isFinishing()) {
            return;
        }
        activity.setProgressBarIndeterminateVisibility(false);
        activity.setProgressBarIndeterminate(false);
    }

    //
    // SpeedTestRecordProcessorTask.OnDataProcessorListener implementations.
    //

    /**
     * {@inheritDoc} <br/>
     * 
     * @see OnDataProcessorListener
     */
    @Override
    public void onPreProcess() {
        // when data is processing, give user feedback by showing progress in AB
        showProgressIndicator();
    }

}
