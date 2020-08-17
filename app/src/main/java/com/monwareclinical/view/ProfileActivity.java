package com.monwareclinical.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.monwareclinical.R;
import com.monwareclinical.dialogs.ChangeEmailDialog;
import com.monwareclinical.dialogs.ChangeNameDialog;
import com.monwareclinical.dialogs.ChangePasswordDialog;
import com.monwareclinical.dialogs.LoadingDialog;
import com.monwareclinical.util.SetUpToolBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements
        View.OnClickListener,
        ChangeNameDialog.DialogListener,
        ChangeEmailDialog.DialogListener,
        ChangePasswordDialog.DialogListener {

    public static final int PICK_IMAGE = 1;

    ProfileActivity fa;

    SetUpToolBar toolBar;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    StorageReference mStorageRef;

    CircleImageView imgProfile;
    TextView txtChangePhoto;
    TextView txtName;
    TextView txtEmail;
    Button btnChangeName;
    Button btnChangePassword;
    Button btnChangeEmail;
    Button btnSignOut;

    LoadingDialog loadingDialog;
    ChangeNameDialog changeNameDialog;
    ChangeEmailDialog changeEmailDialog;
    ChangePasswordDialog changePasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initComps();
        initActions();
        initStuff();
        setUpToolBar();
    }


    void initComps() {
        fa = this;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imgProfile = findViewById(R.id.imgProfilePhoto);
        txtChangePhoto = findViewById(R.id.txtChangeProfilePhoto);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);

        btnChangeName = findViewById(R.id.btnChangeName);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangeEmail = findViewById(R.id.btnChangeEmail);
        btnSignOut = findViewById(R.id.btnSignOut);

        loadingDialog = new LoadingDialog(fa);
        changeNameDialog = new ChangeNameDialog(fa, this);
        changeEmailDialog = new ChangeEmailDialog(fa, this);
        changePasswordDialog = new ChangePasswordDialog(fa, this);
    }

    void initActions() {
        txtChangePhoto.setOnClickListener(this);
        btnChangeName.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        btnChangeEmail.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
    }

    void initStuff() {
        Glide.with(fa).load(mUser.getPhotoUrl()).placeholder(R.drawable.blank_user).dontAnimate().into(imgProfile);
        if (!TextUtils.isEmpty(mUser.getDisplayName()))
            txtName.setText(mUser.getDisplayName());
        else
            txtName.setText("Nombre no establecido");

        txtEmail.setText(mUser.getEmail());
    }

    void setUpToolBar() {
        toolBar = new SetUpToolBar(fa, true, "Perfil", null);
    }

    void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE:
                if (data == null || data.getData() == null) return;

                loadingDialog.showDialog();
                mRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.fb_table_users)).child(mUser.getUid());

                final StorageReference imageRef = mStorageRef.child("profile_photos/" + mUser.getUid());
                UploadTask uploadTask = imageRef.putFile(data.getData());

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUrl = imageRef.getDownloadUrl();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();

                                mUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Glide.with(fa).load(mUser.getPhotoUrl()).placeholder(R.drawable.blank_user).dontAnimate().into(imgProfile);
                                                loadingDialog.dismissDialog();
                                            }
                                        });
                            }
                        });
                    }
                });
                break;
        }
    }

    @Override
    public void getName(String name) {
        changeNameDialog.closeDialog();
        loadingDialog.showDialog();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        mUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingDialog.dismissDialog();
                        initStuff();
                    }
                });
    }

    @Override
    public void getEmail(String oldEmail, String password, final String newEmail) {
        changeEmailDialog.closeDialog();
        loadingDialog.showDialog();

        AuthCredential credential = EmailAuthProvider.getCredential(oldEmail, password);

        mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    mUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingDialog.dismissDialog();
                            if (task.isSuccessful())
                                initStuff();
                            else
                                Toast.makeText(fa, "Intenta nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                else {
                    loadingDialog.dismissDialog();
                    Toast.makeText(fa, "El email o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void getPassword(String txtEmail, String txtOldPassword, final String txtNewPassword) {
        changePasswordDialog.closeDialog();
        loadingDialog.showDialog();

        AuthCredential credential = EmailAuthProvider.getCredential(txtEmail, txtOldPassword);

        mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    mUser.updatePassword(txtNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            loadingDialog.dismissDialog();
                            if (task.isSuccessful()) initStuff();
                            else
                                Toast.makeText(fa, "Intenta nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                else {
                    loadingDialog.dismissDialog();
                    Toast.makeText(fa, "El email o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtChangeProfilePhoto:
                pickFromGallery();
                break;
            case R.id.btnChangeName:
                changeNameDialog.showAlert();
                break;
            case R.id.btnChangePassword:
                changePasswordDialog.showAlert();
                break;
            case R.id.btnChangeEmail:
                changeEmailDialog.showAlert();
                break;
            case R.id.btnSignOut:
                mAuth.signOut();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /*
     ********************FIREBASE********************
     */

    FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(fa, SplashScreenActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}