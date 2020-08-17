package com.monwareclinical.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.monwareclinical.R;
import com.monwareclinical.model.Book;
import com.monwareclinical.model.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ViewHolder> {

    Context context;
    List<Doctor> doctors;
    int selected_position = 0;

    public DoctorsAdapter(Context context, List<Doctor> doctors) {
        this.context = context;
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Doctor doctor = doctors.get(position);
        holder.txtName.setText(doctor.getName());
        Glide.with(context).load(doctor.getImg()).placeholder(R.drawable.blank_user).dontAnimate().into(holder.imgPhoto);

        holder.itemView.setBackgroundColor(selected_position == position ? context.getColor(R.color.colorAccent) : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public String getDrName() {
        return doctors.get(selected_position).getName();
    }

    public String getDrImg() {
        return doctors.get(selected_position).getImg();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtName = itemView.findViewById(R.id.txtName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

                    notifyItemChanged(selected_position);
                    selected_position = getAdapterPosition();
                    notifyItemChanged(selected_position);
                }
            });
        }
    }
}