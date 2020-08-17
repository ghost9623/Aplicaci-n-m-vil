package com.monwareclinical.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.monwareclinical.R;
import com.monwareclinical.adapter.MedicinesAdapter;
import com.monwareclinical.model.Medicine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicinesFragment extends Fragment implements
        TextWatcher,
        MedicinesAdapter.SelectMedicineListener {

    public static final String EXTRA_MEDICINE = "com.monwareclinical.view.EXTRA_MEDICINE";

    View root;
    Context context;

    RecyclerView recyclerView;
    MedicinesAdapter medicinesAdapter;

    List<Medicine> medicines;

    EditText etSearch;

    public static MedicinesFragment newInstance() {
        return new MedicinesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.medicines_fragment, container, false);
        context = root.getContext();

        initComps();
        initActions();
        initStuff();
        initRecycler();

        return root;
    }

    void initComps() {
        recyclerView = root.findViewById(R.id.recyclerView);
        etSearch = root.findViewById(R.id.etSearch);
    }

    void initActions() {
        etSearch.addTextChangedListener(this);
    }

    void initStuff() {
        medicines = new ArrayList<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = mDatabase.child(getString(R.string.fb_table_clinic_medicines));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    medicines.add(ds.getValue(Medicine.class));
                }

                medicinesAdapter = new MedicinesAdapter(context, medicines, MedicinesFragment.this);
                recyclerView.setAdapter(medicinesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onSelectedMedicine(Medicine medicine) {
        Intent intent = new Intent(context, MedicineDetailsActivity.class);
        intent.putExtra(EXTRA_MEDICINE, medicine);
        startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String search = etSearch.getText().toString().trim();

        if (!TextUtils.isEmpty(search)) {
            DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
            Query query = mFirebaseDatabaseReference
                    .child(getString(R.string.fb_table_clinic_medicines))
                    .orderByKey()
                    .startAt(search)
                    .endAt(search+"\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Medicine> medicines = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        //TODO get the data here
                        medicines.add(ds.getValue(Medicine.class));
                    }
                    medicinesAdapter.setMedicines(medicines);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            medicinesAdapter.setMedicines(medicines);
        }
    }
}