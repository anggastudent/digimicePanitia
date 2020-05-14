package com.example.digimiceconferent.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Activity.CheckoutDetail;
import com.example.digimiceconferent.Model.Pending;
import com.example.digimiceconferent.MyUrl;
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Apakah anda ingin membatalkan " + pending.getNameEvent() + "?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.expired(pending.getId());
                    }
                });
                builder.setNegativeButton("Tidak", null);
                builder.show();
                return true;
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

        private void expired(String id) {
            RequestQueue queue = Volley.newRequestQueue(itemView.getContext());
            String url = MyUrl.URL+"/expired-invoice/"+id;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(itemView.getContext(), response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(itemView.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.getCache().clear();
            queue.add(request);
        }
    }
}
