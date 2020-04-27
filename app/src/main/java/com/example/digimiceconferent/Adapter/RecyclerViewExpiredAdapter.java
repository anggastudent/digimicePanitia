package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.Expired;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewExpiredAdapter extends RecyclerView.Adapter<RecyclerViewExpiredAdapter.ExpiredViewHolder> {
    ArrayList<Expired> list = new ArrayList<>();

    public void sendData(ArrayList<Expired> expireds) {
        list.clear();
        list.addAll(expireds);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ExpiredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expired, parent, false);
        return new ExpiredViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpiredViewHolder holder, int position) {
        Expired expired = list.get(position);
        holder.noPembayaran.setText(expired.getId());
        holder.namaPaket.setText(expired.getName());
        holder.maksPeserta.setText(expired.getMaxParticipant());
        holder.harga.setText(expired.getPrice());
        holder.tanggal.setText(expired.getExpired());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExpiredViewHolder extends RecyclerView.ViewHolder {
        TextView noPembayaran, namaPaket, maksPeserta, harga, tanggal;

        public ExpiredViewHolder(@NonNull View itemView) {
            super(itemView);

            noPembayaran = itemView.findViewById(R.id.no_pembayaran_expired);
            namaPaket = itemView.findViewById(R.id.nama_paket_expired);
            maksPeserta = itemView.findViewById(R.id.maks_peserta_expired);
            harga = itemView.findViewById(R.id.harga_expired);
            tanggal = itemView.findViewById(R.id.tanggal_expired);
        }
    }
}
