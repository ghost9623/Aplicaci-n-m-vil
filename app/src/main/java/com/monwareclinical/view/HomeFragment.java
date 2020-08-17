package com.monwareclinical.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.monwareclinical.R;
import com.monwareclinical.adapter.MyBooksAdapter;
import com.monwareclinical.model.Book;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements
        MyBooksAdapter.SelectBookListener {

    View root;
    Context context;

    List<Book> books;

    RecyclerView recyclerView;
    MyBooksAdapter myBooksAdapter;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_fragment, container, false);
        context = root.getContext();

        initComps();
        initActions();
        initStuff();
        initRecycler();

        return root;
    }

    void initComps() {
        recyclerView = root.findViewById(R.id.recyclerView);
    }

    void initActions() {
    }

    void initStuff() {
        books = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        final int myDateInt = Integer.parseInt(dateFormat.format(date).replace("-", ""));

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = mDatabase.child(getString(R.string.fb_table_clinic_books));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String dbDate = ds.getKey();
                    int dbDateInt = Integer.parseInt(dbDate.replace("-", ""));
                    if (myDateInt <= dbDateInt) {
                        for (DataSnapshot dx : ds.getChildren()) {
                            Book b = dx.getValue(Book.class);
                            if (b.getUserID().equals(mUserID)) {
                                books.add(b);
                            }
                        }
                    }
                }

                myBooksAdapter = new MyBooksAdapter(context, books, HomeFragment.this);
                recyclerView.setAdapter(myBooksAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onSelectedBook(int position) {

    }
}