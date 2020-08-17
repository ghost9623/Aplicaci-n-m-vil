package com.monwareclinical.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.monwareclinical.R;
import com.monwareclinical.model.Medicine;

import java.text.DecimalFormat;
import java.util.List;

public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.ViewHolder> {

    Context context;
    List<Medicine> medicines;
    SelectMedicineListener listener;
    final DecimalFormat formatter = new DecimalFormat("#,###.00");

    public MedicinesAdapter(Context context, List<Medicine> medicines, SelectMedicineListener listener) {
        this.context = context;
        this.medicines = medicines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Medicine medicine = medicines.get(position);

        if (!TextUtils.isEmpty(medicine.getImg()))
            Glide.with(context).load(medicine.getImg()).placeholder(R.drawable.logo).dontAnimate().into(holder.imgPhoto);
        else
            holder.imgPhoto.setImageDrawable(context.getDrawable(R.drawable.logo));

        holder.txtName.setText(medicine.getName());
        holder.txtDesc.setText(medicine.getDesc());

        if (medicine.getIsAvailable() == Medicine.MEDICINE_AVAILABLE) {
            holder.txtIsAvailable.setTextColor(context.getColor(R.color.colorAvailable));
            holder.txtIsAvailable.setText("Disponible");
        } else {
            holder.txtIsAvailable.setTextColor(context.getColor(R.color.colorUnavailable));
            holder.txtIsAvailable.setText("No disponible");
        }

        double price=medicine.getPrice();
        String txtP;
        if(position == 0d)
            txtP="0.00";
        else
            txtP=formatter.format(price);

        holder.txtPrice.setText(String.format("$%s", txtP));
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        ImageView imgPhoto;
        TextView txtName;
        TextView txtDesc;
        TextView txtPrice;
        TextView txtIsAvailable;

        SelectMedicineListener listener;

        public ViewHolder(View itemView, SelectMedicineListener listener) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtName = itemView.findViewById(R.id.txtName);
            txtDesc = itemView.findViewById(R.id.txtDescription);
            txtPrice=itemView.findViewById(R.id.txtPrice);
            txtIsAvailable = itemView.findViewById(R.id.txtIsAvailable);

            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onSelectedMedicine(medicines.get(getAdapterPosition()));
        }
    }

    public interface SelectMedicineListener {
        void onSelectedMedicine(Medicine medicine);
    }
}