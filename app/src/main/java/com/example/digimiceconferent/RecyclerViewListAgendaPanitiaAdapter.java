package com.example.digimiceconferent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

public class RecyclerViewListAgendaPanitiaAdapter extends RecyclerView.Adapter<RecyclerViewListAgendaPanitiaAdapter.CardViewViewHolder> {
    ArrayList<EventAgendaPanitia> listAgenda = new ArrayList<>();


    public void sendEventAgendaPanitia(ArrayList<EventAgendaPanitia> eventAgendaPanitias) {
        listAgenda.clear();
        listAgenda.addAll(eventAgendaPanitias);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_agenda, parent, false);
        return new CardViewViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        EventAgendaPanitia eventAgendaPanitia = listAgenda.get(position);
        holder.judul.setText(eventAgendaPanitia.getJudul());
        holder.jam.setText(eventAgendaPanitia.getJam());


    }

    @Override
    public int getItemCount() {
        return listAgenda.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());

    }


    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        TimelineView timelineView;
        TextView judul,jam;


        public CardViewViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            judul = itemView.findViewById(R.id.judulAgendaPanitia);
            jam = itemView.findViewById(R.id.timeAgendaPanitia);
            timelineView = itemView.findViewById(R.id.timelineAgendaPanitia);
            timelineView.initLine(viewType);


        }

    }


}
