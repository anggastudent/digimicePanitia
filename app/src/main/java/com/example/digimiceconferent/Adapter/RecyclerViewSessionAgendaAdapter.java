package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.SessionAgenda;
import com.example.digimiceconferent.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewSessionAgendaAdapter extends RecyclerView.Adapter<RecyclerViewSessionAgendaAdapter.AgendaPanitiaViewHolder> {
    ArrayList<SessionAgenda> listAgenda = new ArrayList<>();


    public void sendDataAgenda(ArrayList<SessionAgenda> agendaPanitias) {
        listAgenda.clear();
        listAgenda.addAll(agendaPanitias);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AgendaPanitiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session_agenda, parent, false);
        return new AgendaPanitiaViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaPanitiaViewHolder holder, int position) {
        SessionAgenda sessionAgenda = listAgenda.get(position);
        holder.judul.setText(sessionAgenda.getJudul());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dateStart = dateFormat.parse(sessionAgenda.getJam());
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("hh:mm");
            holder.jam.setText(dateFormatNew.format(dateStart)+" WIB");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.desc.setText(sessionAgenda.getDesc());


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
        TextView judul,jam, desc;
        TimelineView timelineView;

        public AgendaPanitiaViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            judul = itemView.findViewById(R.id.judulAgendaPanitia);
            jam = itemView.findViewById(R.id.timeAgendaPanitia);
            desc = itemView.findViewById(R.id.descAgendaPanitia);
            timelineView = itemView.findViewById(R.id.timeline);
            timelineView.initLine(viewType);

        }

    }


}
