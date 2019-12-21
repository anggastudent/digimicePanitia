package com.example.digimiceconferent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class RecyclerViewListSessionPanitiaAdapter extends RecyclerView.Adapter<RecyclerViewListSessionPanitiaAdapter.SessionPanitiaViewHolder> {
    ArrayList<EventSessionPanitia> listSession = new ArrayList<>();
    RecyclerViewListAgendaPanitiaAdapter agendaPanitiaAdapter = new RecyclerViewListAgendaPanitiaAdapter();

    public void sendEventSessionPanitia(ArrayList<EventSessionPanitia> eventSessionPanitias) {
        listSession.clear();
        listSession.addAll(eventSessionPanitias);
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
        EventSessionPanitia eventSessionPanitia = listSession.get(position);
        holder.nameSession.setText(eventSessionPanitia.getJudul());

        holder.layoutManager= new LinearLayoutManager(holder.itemView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        holder.rvagenda.setLayoutManager(holder.layoutManager);

        if(eventSessionPanitia.getListAgenda() != null) {
            agendaPanitiaAdapter.sendDataAgenda(eventSessionPanitia.getListAgenda());
            holder.rvagenda.setAdapter(agendaPanitiaAdapter);
            holder.rvagenda.setHasFixedSize(true);
        }

    }

    @Override
    public int getItemCount() {
        return listSession.size();
    }

    public class SessionPanitiaViewHolder extends RecyclerView.ViewHolder {
        TextView nameSession;
        RecyclerView rvagenda;
        LinearLayoutManager layoutManager;


        public SessionPanitiaViewHolder(@NonNull View itemView) {
            super(itemView);

            nameSession = itemView.findViewById(R.id.item_session);
            rvagenda = itemView.findViewById(R.id.rv_eventAgendaPanitia);



        }


    }
}
