package com.monwareclinical.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.monwareclinical.R;
import com.monwareclinical.dialogs.LoadingDialog;
import com.monwareclinical.util.Constants;

public class SignUpActivity extends AppCompatActivity implements
        View.OnClickListener {

    public static final String EXTRA_EMAIL = "com.monwareclinical.view.EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD = "com.monwareclinical.view.EXTRA_PASSWORD";

    public static LoadingDialog loadingDialog;

    FirebaseAuth mAuth;

    Activity fa;

    TextInputLayout tilEtEmail;
    TextInputLayout tilEtPassword;
    TextInputLayout tilEtRepeatPassword;
    Button btnSignUpMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        initComps();
        initActions();
        initStuff();
    }

    void initComps() {
        fa = this;

        tilEtEmail = findViewById(R.id.tilEtEmail);
        tilEtPassword = findViewById(R.id.tilEtPassword);
        tilEtRepeatPassword = findViewById(R.id.tilEtRepeatPassword);
        btnSignUpMe = findViewById(R.id.btnSignUpMe);
    }

    void initActions() {
        btnSignUpMe.setOnClickListener(this);
    }

    void initStuff() {
        loadingDialog = new LoadingDialog(fa);

        mAuth = FirebaseAuth.getInstance();

        tilEtEmail.getEditText().setText("gmcarlosd@hotmail.com");
        tilEtPassword.getEditText().setText("Aderevab1!");
        tilEtRepeatPassword.getEditText().setText("Aderevab1!");
    }

    boolean isSomethingEmpty() {
        String txtEmail = tilEtEmail.getEditText().getText().toString();
        String txtPassword = tilEtPassword.getEditText().getText().toString();
        String txtRepeatPassword = tilEtRepeatPassword.getEditText().getText().toString();

        // Email
        if (TextUtils.isEmpty(txtEmail.trim())) {
            tilEtEmail.getEditText().setError("Este campo no puede estar vacio.");
            tilEtEmail.getEditText().requestFocus();
            return true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            tilEtEmail.getEditText().setError("Email invalido.");
            tilEtEmail.getEditText().requestFocus();
            return true;
        } else {
            tilEtEmail.getEditText().setError(null);
        }
        // Password
        if (TextUtils.isEmpty(txtPassword.trim())) {
            tilEtPassword.getEditText().setError("Este campo no puede estar vacio.");
            tilEtPassword.getEditText().requestFocus();
            return true;
        } else {
            tilEtPassword.getEditText().setError(null);
        }
        // Repeat Password
        if (TextUtils.isEmpty(txtRepeatPassword.trim())) {
            tilEtRepeatPassword.getEditText().setError("Este campo no puede estar vacio.");
            tilEtRepeatPassword.getEditText().requestFocus();
            return true;
        } else {
            tilEtRepeatPassword.getEditText().setError(null);
        }
        return false;
    }

    boolean passwordsMatches() {
        String txtPassword = tilEtPassword.getEditText().getText().toString();
        String txtRepeatPassword = tilEtRepeatPassword.getEditText().getText().toString();

        return txtPassword.equals(txtRepeatPassword);
    }

    void createNewUser(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println(task.getException());
                        if (task.isSuccessful()) {

                            tilEtEmail.getEditText().setError(null);

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName("")
                                    .setPhotoUri(Uri.parse(Constants.URL_DEFAULT_PROFILE_PHOTO))
                                    .build();

                            firebaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            loadingDialog.dismissDialog();
                                            Intent intent = new Intent();
                                            intent.putExtra(EXTRA_EMAIL, email);
                                            intent.putExtra(EXTRA_PASSWORD, password);
                                            setResult(LoginActivity.EXTRA_SIGN_UP, intent);
                                            finish();
                                            fa.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        }
                                    });
                        } else {
                            loadingDialog.dismissDialog();
                            tilEtEmail.getEditText().setError("El correo ya esta registrado");
                            tilEtEmail.getEditText().requestFocus();
//                            Toast.makeText(fa, fa.getString(R.string.fb_auth_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUpMe:

                loadingDialog.showDialog();
                loadingDialog.setText("Checando campos, por favor espera...");
                if (!isSomethingEmpty()) {
                    if (passwordsMatches()) {
                        tilEtPassword.getEditText().setError(null);
                        tilEtRepeatPassword.getEditText().setError(null);

                        final String txtEmail = tilEtEmail.getEditText().getText().toString();
                        final String txtPassword = tilEtPassword.getEditText().getText().toString();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.setText("Registrando usuario, por favor espera...");
                                createNewUser(txtEmail, txtPassword);
                            }
                        }, 1_000L);
                    } else {
                        tilEtPassword.getEditText().setError("Las contraseñas no son iguales");
                        tilEtRepeatPassword.getEditText().setError("Las contraseñas no son iguales");
                    }
                }
                break;
        }
    }
}