package com.monwareclinical.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.monwareclinical.R;
import com.monwareclinical.dialogs.LoadingDialog;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    public static final int EXTRA_SIGN_UP = 1;

    Activity fa;

    LoadingDialog loadingDialog;

    FirebaseAuth mAuth;

    TextInputLayout tilEtEmail;
    TextInputLayout tilEtPassword;
    Button btnLogin;
    Button btnSignUpMe;
    TextView btnForgottenPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComps();
        initActions();
        initStuff();
    }

    void initComps() {
        fa = this;
        mAuth = FirebaseAuth.getInstance();
        tilEtEmail = findViewById(R.id.tilEtEmail);
        tilEtPassword = findViewById(R.id.tilEtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUpMe = findViewById(R.id.btnSignUpMe);
        btnForgottenPassword = findViewById(R.id.forgottenPassword);
    }

    void initActions() {
        btnLogin.setOnClickListener(this);
        btnSignUpMe.setOnClickListener(this);
        btnForgottenPassword.setOnClickListener(this);
    }

    void initStuff() {
        loadingDialog = new LoadingDialog(fa);
        tilEtEmail.getEditText().setText("gmcarlosd@hotmail.com");
        tilEtPassword.getEditText().setText("Aderevab1!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXTRA_SIGN_UP) {
            String txtEmail = data.getStringExtra(SignUpActivity.EXTRA_EMAIL);
            String txtPassword = data.getStringExtra(SignUpActivity.EXTRA_PASSWORD);

            tilEtEmail.getEditText().setText(txtEmail);
            tilEtPassword.getEditText().setText(txtPassword);
        }
    }

    void login() {
        String txtEmail = tilEtEmail.getEditText().getText().toString();
        String txtPassword = tilEtPassword.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(fa, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loadingDialog.dismissDialog();
                    startActivity(new Intent(fa, MenuActivity.class));
                    finish();
                    fa.overridePendingTransition(R.anim.fade_out, R.anim.fade_out);
                } else {
                    loadingDialog.dismissDialog();
                    Toast.makeText(fa, "El correo o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                loadingDialog.showDialog();
                loadingDialog.setText("Iniciando sesión...");
                login();
                break;
            case R.id.btnSignUpMe:
                Intent intent = new Intent(fa, SignUpActivity.class);
                startActivityForResult(intent, EXTRA_SIGN_UP);
                break;
            case R.id.forgottenPassword:
                startActivity(new Intent(fa, ForgottenPasswordActivity.class));
                break;
        }
    }
}