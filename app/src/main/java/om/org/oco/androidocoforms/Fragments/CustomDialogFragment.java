package om.org.oco.androidocoforms.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

    // strings
    Context context;
    String message;
    String okButton;
    String cancelButton;

    public CustomDialogFragment() {
        // default constructor
    }

    @SuppressLint("ValidFragment")
    public CustomDialogFragment(Context context, String message, String okButton, String cancelButton) {
        this.context = context;
        this.message = message;
        this.okButton = okButton;
        this.cancelButton = cancelButton;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton(okButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        }).setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // Create AlertDialog
        return builder.create();
    }
}