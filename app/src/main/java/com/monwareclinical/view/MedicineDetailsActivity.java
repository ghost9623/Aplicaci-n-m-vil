package com.monwareclinical.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.monwareclinical.R;
import com.monwareclinical.model.Medicine;
import com.monwareclinical.util.SetUpToolBar;

public class MedicineDetailsActivity extends AppCompatActivity {

    Activity fa;

    SetUpToolBar toolBar;

    ImageView imgPhoto;
    TextView txtName;
    TextView txtDesc;
    TextView txtIsAvailable;
    TextView txtRouteOfAdministration;
    TextView txtExcretion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);
        initComps();
        initActions();
        initStuff();
    }

    void initComps() {
        fa = this;

        imgPhoto = findViewById(R.id.imgPhoto);
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDescription);
        txtIsAvailable = findViewById(R.id.txtIsAvailable);
        txtRouteOfAdministration = findViewById(R.id.txtRouteOfAdministration);
        txtExcretion = findViewById(R.id.txtExcretion);
    }

    void initActions() {
    }

    void initStuff() {
        Medicine medicine = (Medicine) getIntent().getSerializableExtra(MedicinesFragment.EXTRA_MEDICINE);

        if (!TextUtils.isEmpty(medicine.getImg()))
            Glide.with(fa).load(medicine.getImg()).placeholder(R.drawable.logo).dontAnimate().into(imgPhoto);
        else
            imgPhoto.setImageDrawable(getDrawable(R.drawable.logo));

        txtName.setText(medicine.getName());
        txtDesc.setText(medicine.getDesc());

        if (medicine.getIsAvailable() == Medicine.MEDICINE_AVAILABLE) {
            txtIsAvailable.setTextColor(getColor(R.color.colorAvailable));
            txtIsAvailable.setText("Si");
        } else {
            txtIsAvailable.setTextColor(getColor(R.color.colorUnavailable));
            txtIsAvailable.setText("No");
        }

        txtRouteOfAdministration.setText(medicine.getRouteOfAdministration());
        txtExcretion.setText(medicine.getExcretion());


        // ToolBar
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        toolBar = new SetUpToolBar(fa, true, "Menú", mUser.getPhotoUrl());
        toolBar.setTitle(medicine.getName());
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}