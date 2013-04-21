
package ca.liquidlabs.android.speedtestmapper;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * Simple input dialog for taking custom input.
 * 
 * @see http://android-developers.blogspot.ca/2012/05/using-dialogfragments.html
 *      for dialog fragment guidelines.
 */
public class InputDialogFragment extends DialogFragment implements OnEditorActionListener {

    public interface InputDialogListener {
        void onFinishEditDialog(String inputText);
    }

    private EditText mEditText;

    static InputDialogFragment newInstance() {
        return new InputDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_input, container);
        mEditText = (EditText) view.findViewById(R.id.txt_input_area);
        getDialog().setTitle("Hello");

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            InputDialogListener activity = (InputDialogListener) getActivity();
            activity.onFinishEditDialog(mEditText.getText().toString());
            this.dismiss();
            return true;
        }
        return false;
    }

}
