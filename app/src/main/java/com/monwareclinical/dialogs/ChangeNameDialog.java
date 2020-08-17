package com.monwareclinical.dialogs;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.monwareclinical.R;

public class ChangeNameDialog {

    Context context;
    AlertDialog show;
    DialogListener listener;


    public ChangeNameDialog(Context context, DialogListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.change_name_dialog, null);

        builder
                .setView(view)
                .setCancelable(false)
                .create();


        show = builder.show();

        ImageView btnClose = view.findViewById(R.id.txtClose);
        final EditText etName = view.findViewById(R.id.etName);
        Button btnSave = view.findViewById(R.id.btnSave);


        // Yes
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtName = etName.getText().toString();
                if (TextUtils.isEmpty(txtName.trim())) {
                    etName.setError("Este campo no puede estar vacio");
                    etName.requestFocus();
                    return;
                }
                etName.setError(null);
                listener.getName(txtName);
            }
        });
        // Close
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
            }
        });

    }

    public void closeDialog() {
        show.dismiss();
    }

    public interface DialogListener {
        void getName(String name);
    }
}
