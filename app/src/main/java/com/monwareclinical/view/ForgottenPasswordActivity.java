package com.monwareclinical.view;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.monwareclinical.R;

public class ForgottenPasswordActivity extends AppCompatActivity {

    Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);
    }
}