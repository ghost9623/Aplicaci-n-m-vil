package com.monwareclinical.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.monwareclinical.R;


public class LoadingDialog {

    AlertDialog dialog;

    public LoadingDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog, null));
        dialog = builder.create();
        dialog.setCancelable(false);
    }

    public void setText(String msg) {
        TextView txtMsg = dialog.findViewById(R.id.txtMsg);
        txtMsg.setText(msg);
    }

    public void showDialog() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
