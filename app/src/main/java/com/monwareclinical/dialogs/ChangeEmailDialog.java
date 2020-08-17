package com.monwareclinical.dialogs;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.monwareclinical.R;

public class ChangeEmailDialog {

    Context context;
    AlertDialog show;
    DialogListener listener;


    public ChangeEmailDialog(Context context, DialogListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.change_email_dialog, null);

        builder
                .setView(view)
                .setCancelable(false)
                .create();


        show = builder.show();

        ImageView btnClose = view.findViewById(R.id.txtClose);
        final EditText etOldEmail = view.findViewById(R.id.etOldEmail);
        final EditText etPassword = view.findViewById(R.id.etPassword);
        final EditText etNewEmail = view.findViewById(R.id.etNewEmail);
        Button btnSave = view.findViewById(R.id.btnSave);


        // Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEverythingOk(etOldEmail, etPassword, etNewEmail)) {
                    String txtOldEmail = etOldEmail.getText().toString();
                    String txtPassword = etPassword.getText().toString();
                    String txtNewEmail = etNewEmail.getText().toString();
                    listener.getEmail(txtOldEmail, txtPassword, txtNewEmail);
                }
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

    boolean isEverythingOk(EditText oldEmail, EditText password, EditText newEmail) {

        String txtOldEmail = oldEmail.getText().toString();
        String txtPassword = password.getText().toString();
        String txtNewEmail = newEmail.getText().toString();

        // Old Email
        if (TextUtils.isEmpty(txtOldEmail.trim())) {
            oldEmail.setError("Este campo no puede estar vacio");
            oldEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txtOldEmail).matches()) {
            oldEmail.setError("Email invalido.");
            oldEmail.requestFocus();
            return false;
        }
        // Password
        if (TextUtils.isEmpty(txtPassword.trim())) {
            password.setError("Este campo no puede estar vacio");
            password.requestFocus();
            return false;
        }
        // New Email
        if (TextUtils.isEmpty(txtNewEmail.trim())) {
            newEmail.setError("Este campo no puede estar vacio");
            newEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txtNewEmail).matches()) {
            newEmail.setError("Email invalido.");
            newEmail.requestFocus();
            return false;
        }
        oldEmail.setError(null);
        password.setError(null);
        newEmail.setError(null);
        return true;
    }

    public void closeDialog() {
        show.dismiss();
    }

    public interface DialogListener {
        void getEmail(String oldEmail, String password, String newEmail);
    }
}
