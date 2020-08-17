package com.monwareclinical.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monwareclinical.R;
import com.monwareclinical.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    Context context;
    List<Book> books;
    SelectHourListener listener;

    public BooksAdapter(Context context, SelectHourListener listener) {
        this.context = context;
        books = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hour, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtHour.setText(books.get(position).getHour());

        String txtAvailable;
        switch (books.get(position).getState()) {
            case Book.TOOK:
                txtAvailable = "Ocupado";
                holder.txtIsAvailable.setTextColor(context.getColor(R.color.colorUnavailable));
                break;
            case Book.AVAILABLE:
                txtAvailable = "Disponible";
                holder.txtIsAvailable.setTextColor(context.getColor(R.color.colorAvailable));
                break;
            case Book.SELECTED:
                txtAvailable = "Selecionado";
                holder.txtIsAvailable.setTextColor(context.getColor(R.color.colorSelected));
                break;
            default:
                txtAvailable = "";
                break;
        }
        holder.txtIsAvailable.setText(txtAvailable);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public Book getBookByPosition(int position) {
        return books.get(position);
    }

    public void selectHour(int position) {
        books.get(position).setState(Book.SELECTED);
        notifyItemChanged(position);
    }

    public void cleanSelectedHours() {
        for (Book b : books)
            if (b.getState() == Book.SELECTED)
                b.setState(Book.AVAILABLE);
        notifyDataSetChanged();
    }

    public boolean isHourSelected() {
        for (Book b : books)
            if (b.getState() == Book.SELECTED)
                return true;
        return false;
    }

    public Book getSelectedHour() {
        for (Book b : books)
            if (b.getState() == Book.SELECTED)
                return b;
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView txtHour;
        TextView txtIsAvailable;
        SelectHourListener listener;

        public ViewHolder(View itemView, SelectHourListener onIconClick) {
            super(itemView);

            txtHour = itemView.findViewById(R.id.txtHour);
            txtIsAvailable = itemView.findViewById(R.id.txtIsAvailable);
            listener = onIconClick;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onSelectedBook(getAdapterPosition());
        }
    }

    public interface SelectHourListener {
        void onSelectedBook(int position);
    }
}