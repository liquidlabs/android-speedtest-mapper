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

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import ca.liquidlabs.android.speedtestvisualizer.R;
import ca.liquidlabs.android.speedtestvisualizer.util.Tracer;

/**
 * Simple input dialog for taking custom input.
 * 
 * @see http://android-developers.blogspot.ca/2012/05/using-dialogfragments.html
 *      for dialog fragment guidelines.
 */
public class InputDialogFragment extends DialogFragment implements OnEditorActionListener,
        OnClickListener {
    private static final String LOG_TAG = InputDialogFragment.class.getSimpleName();

    public interface InputDialogListener {
        void onFinishEditDialog(String inputText);
    }

    private EditText mEditText;
    private Button mDoneButton;

    public static InputDialogFragment newInstance() {
        return new InputDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_input, container);
        mEditText = (EditText) view.findViewById(R.id.txt_input_area);
        mDoneButton = (Button) view.findViewById(R.id.btn_process_input_data);

        getDialog().setTitle(getText(R.string.lbl_input_heading));

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);
        mDoneButton.setOnClickListener(this);

        return view;
    }

    /**
     * Notifies parent activity using interface
     * 
     * @param data Data that was input by user
     */
    private void notifyInputData(String data) {
        InputDialogListener activity = (InputDialogListener) getActivity();
        activity.onFinishEditDialog(data);
    }

    //
    // OnEditorActionListener implementation
    //
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            this.notifyInputData(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }

    //
    // View.OnClickListener implementation
    //
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_process_input_data:
                Tracer.debug(LOG_TAG, "PROCESS BUTTON USED");
                this.notifyInputData(mEditText.getText().toString());
                this.dismiss();
                break;
        }
    }

}
