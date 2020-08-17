package com.monwareclinical.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.monwareclinical.R;
import com.monwareclinical.model.Clinic;
import com.monwareclinical.util.Constants;

public class ClinicFragment extends Fragment implements
        View.OnClickListener {

    View root;
    Context context;

    Clinic clinic;

    TextView txtDescription;

    Button btnMakeAnAppointment;
    Button btnPhoneNumber;
    Button btnLocation;

    public static ClinicFragment newInstance() {
        return new ClinicFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.clinic_fragment, container, false);
        context = root.getContext();

        initComps();
        initActions();
        initStuff();

        return root;
    }

    void initComps() {
        txtDescription = root.findViewById(R.id.txtDescription);
        btnMakeAnAppointment = root.findViewById(R.id.btnMakeAnAppointment);
        btnPhoneNumber = root.findViewById(R.id.btnPhoneNumber);
        btnLocation = root.findViewById(R.id.btnLocation);
    }

    void initActions() {
        btnMakeAnAppointment.setOnClickListener(this);
        btnPhoneNumber.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
    }

    void initStuff() {
        clinic = Constants.getInstance(context).getClinic();

        if (clinic != null) {
            String description = clinic.getDescription() + "\n\n" +
                    "Dirección:\n" +
                    clinic.getStreetAddress() + " #" + clinic.getExtNumber() + "\n" +
                    clinic.getState() + ", " + clinic.getCity() + "\n\n" +
                    "Abre a las: " + clinic.getOpensAt() + "\n" +
                    "Cierra a las: " + clinic.getClosesAt();

            txtDescription.setText(description);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnMakeAnAppointment:
                startActivity(new Intent(context, MakeAppointmentActivity.class));
                break;
            case R.id.btnPhoneNumber:
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + clinic.getPhoneNumber()));
                startActivity(i);
                break;
            case R.id.btnLocation:
                String streetAddress = clinic.getStreetAddress();
                String extNumber = clinic.getExtNumber();

                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + streetAddress + " " + extNumber);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null)
                    startActivity(mapIntent);
                else
                    Toast.makeText(context, "No se encontro la aplicación Google Maps", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}