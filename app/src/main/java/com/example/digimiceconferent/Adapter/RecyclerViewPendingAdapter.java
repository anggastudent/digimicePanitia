package com.example.digimiceconferent.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Activity.CheckoutDetail;
import com.example.digimiceconferent.Model.Pending;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewPendingAdapter extends RecyclerView.Adapter<RecyclerViewPendingAdapter.PendingViewHolder> {
    ArrayList<Pending> list = new ArrayList<>();

    public void sendData(ArrayList<Pending> pendings) {
        list.clear();
        list.addAll(pendings);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending, parent, false);
        return new PendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingViewHolder holder, int position) {
        Pending pending = list.get(position);
        holder.noPembayaran.setText(pending.getId());
        holder.namaPaket.setText(pending.getName());
        holder.maksPeserta.setText(pending.getMaxParticipant());
        holder.harga.setText(pending.getPrice());
        holder.tanggal.setText(pending.getExpired());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), CheckoutDetail.class);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PendingViewHolder extends RecyclerView.ViewHolder {
        TextView noPembayaran, namaPaket, maksPeserta, harga, tanggal;
        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);

            noPembayaran = itemView.findViewById(R.id.no_pembayaran_pending);
            namaPaket = itemView.findViewById(R.id.nama_paket_pending);
            maksPeserta = itemView.findViewById(R.id.maks_peserta_pending);
            harga = itemView.findViewById(R.id.harga_pending);
            tanggal = itemView.findViewById(R.id.tanggal_pending);
        }
    }
}
