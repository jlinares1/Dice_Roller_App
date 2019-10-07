package edu.ggc.lutz.diceroller_solution;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;


public class RollLengthDialogFragment extends DialogFragment {

    public interface OnRollLengthSelectedListener {
        void onRollLengthClick(int which);
    }

    private OnRollLengthSelectedListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_roll_length);
        builder.setItems(R.array.length_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mListener.onRollLengthClick(which);
                // 'which' is the index position chosen
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnRollLengthSelectedListener) activity;
    }


}
