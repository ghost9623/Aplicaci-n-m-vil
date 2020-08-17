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

public class ChangePasswordDialog {

    Context context;
    AlertDialog show;
    DialogListener listener;


    public ChangePasswordDialog(Context context, DialogListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.change_password_dialog, null);

        builder
                .setView(view)
                .setCancelable(false)
                .create();


        show = builder.show();

        ImageView btnClose = view.findViewById(R.id.txtClose);
        final EditText etEmail = view.findViewById(R.id.etEmail);
        final EditText etOldPassword = view.findViewById(R.id.etOldPassword);
        final EditText etNewPassword = view.findViewById(R.id.etNewPassword);
        final EditText etRepeatNewPassword = view.findViewById(R.id.etRepeatNewPassword);
        Button btnSave = view.findViewById(R.id.btnSave);


        // Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEverythingOk(etEmail, etOldPassword, etNewPassword, etRepeatNewPassword)) {
                    String txtEmail = etEmail.getText().toString();
                    String txtOldPassword = etOldPassword.getText().toString();
                    String txtNewPassword = etNewPassword.getText().toString();

                    listener.getPassword(txtEmail, txtOldPassword, txtNewPassword);
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

    boolean isEverythingOk(EditText email, EditText oldPassword, EditText newPassword, EditText repeatNewPassword) {
        String txtEmail = email.getText().toString();
        String txtOldPassword = oldPassword.getText().toString();
        String txtNewPassword = newPassword.getText().toString();
        String txtRepeatNewPassword = repeatNewPassword.getText().toString();

        // Old Email
        if (TextUtils.isEmpty(txtEmail.trim())) {
            email.setError("Este campo no puede estar vacio");
            email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            email.setError("Email invalido.");
            email.requestFocus();
            return false;
        }
        // Old Password
        if (TextUtils.isEmpty(txtOldPassword.trim())) {
            oldPassword.setError("Este campo no puede estar vacio");
            oldPassword.requestFocus();
            return false;
        }
        // New Password
        if (TextUtils.isEmpty(txtNewPassword.trim())) {
            newPassword.setError("Este campo no puede estar vacio");
            newPassword.requestFocus();
            return false;
        }
        // Repeat New Password
        if (TextUtils.isEmpty(txtRepeatNewPassword.trim())) {
            repeatNewPassword.setError("Este campo no puede estar vacio");
            repeatNewPassword.requestFocus();
            return false;
        }
        // Passwords matches
        if (!txtNewPassword.equals(txtRepeatNewPassword)) {
            newPassword.setError("Las contraseñas no coinciden");
            repeatNewPassword.setError("Las contraseñas no coinciden");
            return false;
        }

        email.setError(null);
        oldPassword.setError(null);
        newPassword.setError(null);
        repeatNewPassword.setError(null);
        return true;
    }

    public void closeDialog() {
        show.dismiss();
    }

    public interface DialogListener {
        void getPassword(String txtEmail, String txtOldPassword, String txtNewPassword);
    }
}
