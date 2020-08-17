package com.monwareclinical.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.annotations.NotNull;
import com.monwareclinical.R;
import com.monwareclinical.view.ProfileActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpToolBar implements
        View.OnClickListener {

    Activity fa;

    Toolbar toolbar;
    ImageView btnBackArrow;
    ImageView imgLogo;
    TextView txtTitle;
    CircleImageView imgProfile;

    public SetUpToolBar(Activity fa, boolean isBackArrow, String title, Uri imgUrl) {
        this.fa = fa;
        toolbar = this.fa.findViewById(R.id.toolbar);
        btnBackArrow = this.fa.findViewById(R.id.btnBackArrow);
        imgLogo = this.fa.findViewById(R.id.imgLogo);
        txtTitle = this.fa.findViewById(R.id.txtTitle);
        imgProfile = this.fa.findViewById(R.id.imgProfile);

        if (isBackArrow) {
            btnBackArrow.setVisibility(View.VISIBLE);
            btnBackArrow.setOnClickListener(this);
        } else {
            btnBackArrow.setVisibility(View.GONE);
        }

        txtTitle.setText(title);

        if (imgUrl != null) {
            Glide.with(fa).load(imgUrl).placeholder(R.drawable.blank_user).dontAnimate().into(imgProfile);
            imgProfile.setOnClickListener(this);
        } else {
            imgProfile.setVisibility(View.GONE);
        }
    }

    public void setTitle(@NotNull String title) {
        this.txtTitle.setText(title);
    }

    public void reloadPhoto(@NotNull Uri imgUrl) {
        Glide.with(fa).load(imgUrl).placeholder(R.drawable.blank_user).dontAnimate().into(imgProfile);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackArrow:
                fa.onBackPressed();
                break;
            case R.id.imgProfile:
                fa.startActivity(new Intent(fa, ProfileActivity.class));
                fa.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }
}