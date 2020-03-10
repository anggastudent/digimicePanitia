package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewEventSessionAdapter extends RecyclerView.Adapter<RecyclerViewEventSessionAdapter.SessionPanitiaViewHolder> {
    ArrayList<EventSession> listSession = new ArrayList<>();
    RecyclerViewEventAgendaAdapter agendaPanitiaAdapter = new RecyclerViewEventAgendaAdapter();

    public void sendEventSessionPanitia(ArrayList<EventSession> eventSessions) {
        listSession.clear();
        listSession.addAll(eventSessions);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SessionPanitiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_session, parent, false);
        return new SessionPanitiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionPanitiaViewHolder holder, int position) {
        EventSession eventSession = listSession.get(position);
        holder.nameSession.setText(eventSession.getJudul());
        agendaPanitiaAdapter.sendDataAgenda(eventSession.getListAgenda());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.rvagenda.setAdapter(agendaPanitiaAdapter);
        holder.rvagenda.setHasFixedSize(true);
        holder.rvagenda.setLayoutManager(linearLayoutManager);
    }

    @Override
    public int getItemCount() {
        return listSession.size();
    }

    public class SessionPanitiaViewHolder extends RecyclerView.ViewHolder {
        TextView nameSession;
        RecyclerView rvagenda;


        public SessionPanitiaViewHolder(@NonNull View itemView) {
            super(itemView);

            nameSession = itemView.findViewById(R.id.item_session);
            rvagenda = itemView.findViewById(R.id.rv_eventAgendaPanitia);
        }

    }
}
