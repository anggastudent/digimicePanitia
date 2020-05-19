package com.example.digimiceconferent.Adapter;

import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Activity.CheckoutDetail;
import com.example.digimiceconferent.Model.Paid;
import com.example.digimiceconferent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public void onBindViewHolder(@NonNull final PaidViewHolder holder, int position) {
        final Paid paid = list.get(position);

        if (paid.getId().equals("no invoice")) {
            holder.noPembayaran.setVisibility(View.GONE);
        }
        holder.noPembayaran.setText("No. "+paid.getId());
        holder.namaPaket.setText(paid.getName());
        holder.maksPeserta.setText(paid.getMaxParticipant()+" Maksimal Peserta");
        holder.harga.setText("Rp. "+paid.getPrice());
        holder.namaEvent.setText(paid.getNameEvent());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date dateExp = dateFormat.parse(paid.getPaid());
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy");
            holder.tanggal.setText(dateFormatNew.format(dateExp));

        } catch (ParseException e) {
            e.printStackTrace();
        }
       //holder.tanggal.setText(paid.getPaid());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                Intent intent = new Intent(holder.itemView.getContext(), CheckoutDetail.class);
                intent.putExtra(CheckoutDetail.EXTRA_PAID,paid);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaidViewHolder extends RecyclerView.ViewHolder {
        TextView noPembayaran, namaPaket, namaEvent, maksPeserta, harga, tanggal;

        public PaidViewHolder(@NonNull View itemView) {
            super(itemView);

            noPembayaran = itemView.findViewById(R.id.no_pembayaran_paid);
            namaPaket = itemView.findViewById(R.id.nama_paket_paid);
            maksPeserta = itemView.findViewById(R.id.maks_peserta_paid);
            harga = itemView.findViewById(R.id.harga_paid);
            tanggal = itemView.findViewById(R.id.tanggal_paid);
            namaEvent = itemView.findViewById(R.id.nama_event_paid);
        }

    }
}
