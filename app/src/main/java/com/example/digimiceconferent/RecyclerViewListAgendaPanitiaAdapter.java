package com.example.digimiceconferent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

public class RecyclerViewListAgendaPanitiaAdapter extends RecyclerView.Adapter<RecyclerViewListAgendaPanitiaAdapter.AgendaPanitiaViewHolder> {
    ArrayList<EventAgendaPanitia> listAgenda = new ArrayList<>();


    public void sendDataAgenda(ArrayList<EventAgendaPanitia> agendaPanitias) {
        listAgenda.clear();
        listAgenda.addAll(agendaPanitias);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AgendaPanitiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_agenda, parent, false);
        return new AgendaPanitiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaPanitiaViewHolder holder, int position) {
        EventAgendaPanitia eventAgendaPanitia = listAgenda.get(position);
        holder.judul.setText(eventAgendaPanitia.getJudul());
        holder.jam.setText(eventAgendaPanitia.getJam());


    }

    @Override
    public int getItemCount() {
        return listAgenda.size();
    }


    public class AgendaPanitiaViewHolder extends RecyclerView.ViewHolder {
        TextView judul,jam;

        public AgendaPanitiaViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judulAgendaPanitia);
            jam = itemView.findViewById(R.id.timeAgendaPanitia);




        }

    }


}
