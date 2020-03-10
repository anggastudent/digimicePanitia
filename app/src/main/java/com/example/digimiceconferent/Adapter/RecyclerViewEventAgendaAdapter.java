package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.EventAgenda;
import com.example.digimiceconferent.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

public class RecyclerViewEventAgendaAdapter extends RecyclerView.Adapter<RecyclerViewEventAgendaAdapter.AgendaPanitiaViewHolder> {
    ArrayList<EventAgenda> listAgenda = new ArrayList<>();


    public void sendDataAgenda(ArrayList<EventAgenda> agendaPanitias) {
        listAgenda.clear();
        listAgenda.addAll(agendaPanitias);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AgendaPanitiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_agenda, parent, false);
        return new AgendaPanitiaViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaPanitiaViewHolder holder, int position) {
        EventAgenda eventAgenda = listAgenda.get(position);
        holder.judul.setText(eventAgenda.getJudul());
        holder.jam.setText(eventAgenda.getJam());


    }

    @Override
    public int getItemCount() {
        return listAgenda.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    public class AgendaPanitiaViewHolder extends RecyclerView.ViewHolder {
        TextView judul,jam;
        TimelineView timelineView;

        public AgendaPanitiaViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            judul = itemView.findViewById(R.id.judulAgendaPanitia);
            jam = itemView.findViewById(R.id.timeAgendaPanitia);
            timelineView = itemView.findViewById(R.id.timeline);
            timelineView.initLine(viewType);

        }

    }


}
