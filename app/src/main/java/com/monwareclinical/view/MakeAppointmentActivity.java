package com.monwareclinical.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.monwareclinical.R;
import com.monwareclinical.adapter.BooksAdapter;
import com.monwareclinical.adapter.DoctorsAdapter;
import com.monwareclinical.dialogs.LoadingDialog;
import com.monwareclinical.model.Book;
import com.monwareclinical.model.Clinic;
import com.monwareclinical.model.Doctor;
import com.monwareclinical.util.Constants;
import com.monwareclinical.util.SetUpToolBar;
import com.ncorti.slidetoact.SlideToActView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MakeAppointmentActivity extends AppCompatActivity implements
        View.OnClickListener,
        SlideToActView.OnSlideCompleteListener,
        BooksAdapter.SelectHourListener {

    final String ZERO = "0";
    final String SEPARATOR = "-";
    @SuppressLint("SimpleDateFormat")
    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    Activity fa;
    FirebaseUser mUser;
    SetUpToolBar toolBar;
    SlideToActView slider;

    EditText etTitle;
    EditText etDate;
    RecyclerView recyclerViewDoctors;
    DoctorsAdapter doctorsAdapter;
    RecyclerView recyclerView;
    BooksAdapter booksAdapter;

    final Calendar c = Calendar.getInstance();
    final int mDay = c.get(Calendar.DAY_OF_MONTH);
    final int mMonth = c.get(Calendar.MONTH);
    final int mYear = c.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        initComps();
        initActions();
        initStuff();
        initDoctorsRecycler();
        initRecycler();
    }

    void initComps() {
        fa = this;
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        etTitle = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewDoctors = findViewById(R.id.recyclerViewDoctors);

        slider = findViewById(R.id.sbConfirmation);
    }

    void initActions() {
        etDate.setOnClickListener(this);
        slider.setOnSlideCompleteListener(this);
    }

    void initStuff() {
        toolBar = new SetUpToolBar(fa, true, "Agendar cita", null);
        slider.setOuterColor(getColor(R.color.colorSliderUnlocked));
        slider.setIconColor(getColor(R.color.colorSliderUnlocked));
        slider.setLocked(false);
    }

    void initDoctorsRecycler() {
        final List<Doctor> doctors = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(fa, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDoctors.setLayoutManager(linearLayoutManager);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child(getString(R.string.fb_table_doctors));

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    doctors.add(ds.getValue(Doctor.class));
                }
                doctorsAdapter = new DoctorsAdapter(fa, doctors);
                recyclerViewDoctors.setAdapter(doctorsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void initRecycler() {
        booksAdapter = new BooksAdapter(fa, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(booksAdapter);
    }

    void loadHours(String selectedDate) throws ParseException {
        final LoadingDialog loadingDialog = new LoadingDialog(fa);
        loadingDialog.showDialog();
        loadingDialog.setText("Cargando, porfavor espera...");

        final List<Book> books = new ArrayList<>();

        Clinic clinic = Constants.getInstance(fa).getClinic();

        String opensAt = selectedDate + " " + clinic.getOpensAt();
        String closesAt = selectedDate + " " + clinic.getClosesAt();

        Date date;

        date = sdf.parse(opensAt);
        final Calendar openCalendar = Calendar.getInstance();
        openCalendar.setTime(date);

        date = sdf.parse(closesAt);
        Calendar closesCalendar = Calendar.getInstance();
        closesCalendar.setTime(date);

        long workableHours = openCalendar.getTimeInMillis() - closesCalendar.getTimeInMillis();
        int totalHours = (int) ((workableHours / (1000 * 60 * 60)) % 24);

        if (totalHours < 0)
            totalHours *= -1;

        final int th = (int) (totalHours / .5);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child(getString(R.string.fb_table_clinic_books))
                .child(etDate.getText().toString());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Book> booksTook = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    booksTook.add(ds.getValue(Book.class));
                }

                for (int i = 0; i < th; i++) {
                    int h = openCalendar.get(Calendar.HOUR_OF_DAY);
                    int m = openCalendar.get(Calendar.MINUTE);

                    openCalendar.add(Calendar.MINUTE, 30);

                    int h1 = openCalendar.get(Calendar.HOUR_OF_DAY);
                    int m1 = openCalendar.get(Calendar.MINUTE);

                    String h_m;
                    String h_m1;

                    if (m == 0) {
                        h_m = h + ":00";
                    } else {
                        h_m = h + ":" + m;
                    }
                    if (m1 == 0) {
                        h_m1 = h1 + ":00";
                    } else {
                        h_m1 = h1 + ":" + m1;
                    }

                    String tmp = h_m + " - " + h_m1;

                    Book book;
                    if (isHourTook(booksTook, tmp)) {
                        book = new Book("", "", "", tmp, Book.TOOK, "", "");
                    } else {
                        book = new Book("", "", "", tmp, Book.AVAILABLE, "", "");
                    }
                    books.add(book);
                }
                loadingDialog.dismissDialog();
                booksAdapter.setBooks(books);
            }

            boolean isHourTook(List<Book> booksTook, String hour) {
                for (Book b : booksTook)
                    if (b.getHour().equals(hour))
                        return true;
                return false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etDate:
                DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        final int actualMonth = month + 1;
                        String formattedDay = (dayOfMonth < 10) ? ZERO + dayOfMonth : String.valueOf(dayOfMonth);
                        String formattedMonth = (actualMonth < 10) ? ZERO + actualMonth : String.valueOf(actualMonth);
                        String formattedDate = formattedDay + SEPARATOR + formattedMonth + SEPARATOR + year;
                        etDate.setText(formattedDate);
                        try {
                            loadHours(formattedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateListener, mYear, mMonth, mDay);

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());

                datePickerDialog.getDatePicker().setMinDate(cal.getTime().getTime());
                cal.add(Calendar.DAY_OF_MONTH, 7);
                datePickerDialog.getDatePicker().setMaxDate(cal.getTime().getTime());

                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void onSlideComplete(SlideToActView slideToActView) {
        String title = etTitle.getText().toString();
        final String date = etDate.getText().toString();

        if (TextUtils.isEmpty(title.trim())) {
            etTitle.setError("Este campo no puede estar vacio");
            slider.resetSlider();
            return;
        }
        etTitle.setError(null);

        if (TextUtils.isEmpty(date.trim())) {
            Toast.makeText(fa, "Seleciona una fecha", Toast.LENGTH_SHORT).show();
            slider.resetSlider();
            return;
        }
        if (!booksAdapter.isHourSelected()) {
            Toast.makeText(fa, "Seleciona una hora", Toast.LENGTH_SHORT).show();
            slider.resetSlider();
            return;
        }

        final LoadingDialog loadingDialog = new LoadingDialog(fa);
        loadingDialog.showDialog();
        loadingDialog.setText("Registrando, porfavor espera...");
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.fb_table_clinic_books))
                .child(date);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int maxID = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey() != null)
                        if (Integer.parseInt(ds.getKey()) > maxID)
                            maxID = Integer.parseInt(ds.getKey());
                }
                maxID++;

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                        .child(getString(R.string.fb_table_clinic_books))
                        .child(date)
                        .child(String.valueOf(maxID));

                final Book book = new Book(
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        etTitle.getText().toString(),
                        etDate.getText().toString(),
                        booksAdapter.getSelectedHour().getHour(),
                        Book.TOOK,
                        doctorsAdapter.getDrImg(),
                        doctorsAdapter.getDrName());

                myRef.setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingDialog.dismissDialog();
                        if (task.isSuccessful()) {
                            Toast.makeText(fa, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            fa.onBackPressed();
                        } else {
                            Toast.makeText(fa, "Intenta nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onSelectedBook(int position) {
        Book book = booksAdapter.getBookByPosition(position);
        switch (book.getState()) {
            case Book.TOOK:
                Toast.makeText(fa, "Esa hora se encuentra ocupada", Toast.LENGTH_SHORT).show();
                break;
            case Book.AVAILABLE:
            case Book.SELECTED:
                booksAdapter.cleanSelectedHours();
                booksAdapter.selectHour(position);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}