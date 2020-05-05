package com.example.digimiceconferent.Adapter;

import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Activity.RekapitulasiPeserta;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewSessionRekapitulasiAdapter extends RecyclerView.Adapter<RecyclerViewSessionRekapitulasiAdapter.RekapitulasiViewHolder> {
    ArrayList<EventSession> list = new ArrayList<>();

    public void sendData(ArrayList<EventSession> eventSessions) {
        list.clear();
        list.addAll(eventSessions);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RekapitulasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);
        return new RekapitulasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RekapitulasiViewHolder holder, int position) {
        final EventSession eventSession = list.get(position);
        holder.namaSesi.setText(eventSession.getJudul());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                Intent intent = new Intent(holder.itemView.getContext(), RekapitulasiPeserta.class);
                intent.putExtra(RekapitulasiPeserta.EXTRA_REKAPUTILASI, eventSession);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RekapitulasiViewHolder extends RecyclerView.ViewHolder {
        TextView namaSesi;
        public RekapitulasiViewHolder(@NonNull View itemView) {
            super(itemView);
            namaSesi = itemView.findViewById(R.id.name_session);
        }
    }
}
