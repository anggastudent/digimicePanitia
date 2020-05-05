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
import com.example.digimiceconferent.Model.Pending;
import com.example.digimiceconferent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        final Pending pending = list.get(position);
        holder.noPembayaran.setText(pending.getId());
        holder.namaPaket.setText(pending.getName());
        holder.maksPeserta.setText(pending.getMaxParticipant()+" Maksimal Peserta");
        holder.harga.setText("Rp. "+pending.getPrice());
        holder.namaEvent.setText(pending.getNameEvent());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date dateExp = dateFormat.parse(pending.getPending());
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("HH:mm '/' dd MMMM yyyy");
            holder.tanggal.setText("Terakhir "+dateFormatNew.format(dateExp));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //holder.tanggal.setText(pending.getExpired());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                Intent intent = new Intent(holder.itemView.getContext(), CheckoutDetail.class);
                intent.putExtra(CheckoutDetail.EXTRA_PENDING, pending);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PendingViewHolder extends RecyclerView.ViewHolder {
        TextView noPembayaran, namaPaket, namaEvent, maksPeserta, harga, tanggal;
        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);

            noPembayaran = itemView.findViewById(R.id.no_pembayaran_pending);
            namaPaket = itemView.findViewById(R.id.nama_paket_pending);
            maksPeserta = itemView.findViewById(R.id.maks_peserta_pending);
            harga = itemView.findViewById(R.id.harga_pending);
            tanggal = itemView.findViewById(R.id.tanggal_pending);
            namaEvent = itemView.findViewById(R.id.nama_event_pending);
        }
    }
}
