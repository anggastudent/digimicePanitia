package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.Rekapitulasi;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewRekapitulasiAdapter extends RecyclerView.Adapter<RecyclerViewRekapitulasiAdapter.RekapitulasiViewHolder> {

    ArrayList<Rekapitulasi> list = new ArrayList<>();

    public void sendData(ArrayList<Rekapitulasi> rekapitulasi) {
        list.clear();
        list.addAll(rekapitulasi);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RekapitulasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rekapitulasi, parent, false);
        return new RekapitulasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RekapitulasiViewHolder holder, int position) {
        Rekapitulasi rekapitulasi = list.get(position);
        holder.nameUser.setText(rekapitulasi.getName());
        holder.emailUser.setText(rekapitulasi.getEmail());
        holder.phoneUser.setText(rekapitulasi.getPhone());
        holder.pembayaranUser.setText(rekapitulasi.getPaymentStatus());
        holder.kehadiranUser.setText(rekapitulasi.getRekap());
        holder.provinsiUser.setText(rekapitulasi.getProvinsi());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RekapitulasiViewHolder extends RecyclerView.ViewHolder {
        TextView nameUser, emailUser, phoneUser,provinsiUser, kehadiranUser, pembayaranUser;

        public RekapitulasiViewHolder(@NonNull View itemView) {
            super(itemView);
            nameUser = itemView.findViewById(R.id.name_user);
            emailUser = itemView.findViewById(R.id.email_user);
            phoneUser = itemView.findViewById(R.id.phone_user);
            provinsiUser = itemView.findViewById(R.id.provinsi_user);
            kehadiranUser = itemView.findViewById(R.id.kehadiran_user);
            pembayaranUser = itemView.findViewById(R.id.pembayaran_user);
        }
    }
}
