package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.Paid;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewPaidAdapter extends RecyclerView.Adapter<RecyclerViewPaidAdapter.PaidViewHolder> {
    ArrayList<Paid> list = new ArrayList<>();

    public void sendData(ArrayList<Paid> paids) {
        list.clear();
        list.addAll(paids);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paid, parent, false);
        return new PaidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaidViewHolder holder, int position) {
        Paid paid = list.get(position);
        holder.noPembayaran.setText(paid.getId());
        holder.namaPaket.setText(paid.getName());
        holder.maksPeserta.setText(paid.getMaxParticipant());
        holder.harga.setText(paid.getPrice());
        holder.tanggal.setText(paid.getPaid());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaidViewHolder extends RecyclerView.ViewHolder {
        TextView noPembayaran, namaPaket, maksPeserta, harga, tanggal;

        public PaidViewHolder(@NonNull View itemView) {
            super(itemView);

            noPembayaran = itemView.findViewById(R.id.no_pembayaran_paid);
            namaPaket = itemView.findViewById(R.id.nama_paket_paid);
            maksPeserta = itemView.findViewById(R.id.maks_peserta_paid);
            harga = itemView.findViewById(R.id.harga_paid);
            tanggal = itemView.findViewById(R.id.tanggal_paid);
        }
    }
}
