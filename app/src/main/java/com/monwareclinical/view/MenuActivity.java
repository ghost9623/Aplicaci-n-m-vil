package com.monwareclinical.view;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.monwareclinical.R;
import com.monwareclinical.util.Constants;
import com.monwareclinical.util.SetUpToolBar;

import java.util.List;

public class MenuActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    Activity fa;

    SetUpToolBar toolBar;
    BottomNavigationView bottomNavigationView;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Dexter.withContext(fa)
                .withPermissions(
                        Manifest.permission.WRITE_CALENDAR,
                        Manifest.permission.READ_CALENDAR)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();

        initComps();
        initActions();
        initStuff();
    }

    void initComps() {
        fa = this;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    void initActions() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    void initStuff() {
        toolBar = new SetUpToolBar(fa, false, "Menú", mUser.getPhotoUrl());
        toolBar.setTitle(getString(R.string.menu_home));
        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                toolBar.setTitle(getString(R.string.menu_home));
                fragment = HomeFragment.newInstance();
                break;
            case R.id.navigation_clinic:
                toolBar.setTitle(getString(R.string.menu_building) + " " + Constants.getInstance(fa).getClinic().getName());
                fragment = ClinicFragment.newInstance();
                break;
            case R.id.navigation_medicines:
                toolBar.setTitle(getString(R.string.menu_medicines));
                fragment = MedicinesFragment.newInstance();
                break;
        }

        return loadFragment(fragment);
    }

    boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}