package com.monwareclinical.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.monwareclinical.R;
import com.monwareclinical.model.Book;
import com.monwareclinical.model.Clinic;
import com.monwareclinical.util.Constants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyBooksAdapter extends RecyclerView.Adapter<MyBooksAdapter.ViewHolder> {

    Context context;
    List<Book> books;
    SelectBookListener listener;

    public MyBooksAdapter(Context context, List<Book> books, SelectBookListener listener) {
        this.context = context;
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_book, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Book book = books.get(position);
        Clinic clinic = Constants.getInstance(context).getClinic();

//        String title = book.getTitle();
//        String desc = "Fecha: " + book.getDate() + "\n" +
//                "Hora: " + book.getHour() + "\n" +
//                "Lugar: " + clinic.getName() + "\n" +
//                "Dirección:\n" +
//                clinic.getStreetAddress() + " #" + clinic.getExtNumber() + "\n" +
//                clinic.getState() + ", " + clinic.getCity() + "\n\n";


        Glide.with(context).load(book.getDrImg()).placeholder(R.drawable.blank_user).dontAnimate().into(holder.imgDrProfile);
        holder.txtDrName.setText(book.getDrName());
        holder.txtTitle.setText(book.getTitle());
        holder.txtDate.setText(book.getDate());
        holder.txtHour.setText(book.getHour());
        holder.txtPlace.setText(clinic.getName());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        CircleImageView imgDrProfile;
        TextView txtDrName;
        TextView txtTitle;
        TextView txtDate;
        TextView txtHour;
        TextView txtPlace;

        SelectBookListener listener;

        public ViewHolder(View itemView, SelectBookListener listener) {
            super(itemView);

            imgDrProfile = itemView.findViewById(R.id.imgDrProfile);
            txtDrName = itemView.findViewById(R.id.txtDrName);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtHour = itemView.findViewById(R.id.txtHour);
            txtPlace = itemView.findViewById(R.id.txtPlace);

            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onSelectedBook(getAdapterPosition());
        }
    }

    public interface SelectBookListener {
        void onSelectedBook(int position);
    }
}