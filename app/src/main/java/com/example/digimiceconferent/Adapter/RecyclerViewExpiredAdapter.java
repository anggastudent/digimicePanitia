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
import com.example.digimiceconferent.Model.Expired;
import com.example.digimiceconferent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public void onBindViewHolder(@NonNull final ExpiredViewHolder holder, int position) {
        final Expired expired = list.get(position);
        holder.noPembayaran.setText(expired.getId());
        holder.namaPaket.setText(expired.getName());
        holder.maksPeserta.setText(expired.getMaxParticipant()+" Maksimal Peserta");
        holder.harga.setText("Rp. "+expired.getPrice());
        holder.namaEvent.setText(expired.getNameEvent());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date dateExp = dateFormat.parse(expired.getExpired());
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy");
            holder.tanggal.setText("Dibatalkan "+dateFormatNew.format(dateExp));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //holder.tanggal.setText(expired.getExpired());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                Intent intent = new Intent(holder.itemView.getContext(), CheckoutDetail.class);
                intent.putExtra(CheckoutDetail.EXTRA_EXPIRED, expired);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExpiredViewHolder extends RecyclerView.ViewHolder {
        TextView noPembayaran, namaPaket, namaEvent, maksPeserta, harga, tanggal;

        public ExpiredViewHolder(@NonNull View itemView) {
            super(itemView);

            noPembayaran = itemView.findViewById(R.id.no_pembayaran_expired);
            namaPaket = itemView.findViewById(R.id.nama_paket_expired);
            maksPeserta = itemView.findViewById(R.id.maks_peserta_expired);
            harga = itemView.findViewById(R.id.harga_expired);
            tanggal = itemView.findViewById(R.id.tanggal_expired);
            namaEvent = itemView.findViewById(R.id.nama_event_expired);
        }
    }
}
