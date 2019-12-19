package com.example.digimiceconferent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class RecyclerViewListSessionPanitiaAdapter extends RecyclerView.Adapter<RecyclerViewListSessionPanitiaAdapter.CardViewViewHolder> {
    ArrayList<EventSessionPanitia> listSession = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public void sendEventSessionPanitia(ArrayList<EventSessionPanitia> eventSessionPanitias) {
        listSession.clear();
        listSession.addAll(eventSessionPanitias);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_session, parent, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        EventSessionPanitia eventSessionPanitia = listSession.get(position);
        holder.nameSession.setText(eventSessionPanitia.getJudul());

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        layoutManager.setInitialPrefetchItemCount(eventSessionPanitia.getListAgenda().size());

        RecyclerViewListAgendaPanitiaAdapter agendaPanitiaAdapter = new RecyclerViewListAgendaPanitiaAdapter();
        agendaPanitiaAdapter.sendEventAgendaPanitia(eventSessionPanitia.getListAgenda());
        holder.rvagenda.setLayoutManager(layoutManager);
        holder.rvagenda.setAdapter(agendaPanitiaAdapter);
        holder.rvagenda.setRecycledViewPool(viewPool);

    }

    @Override
    public int getItemCount() {
        return listSession.size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        TextView nameSession;
        RecyclerView rvagenda;
        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);

            nameSession = itemView.findViewById(R.id.item_session);
            rvagenda = itemView.findViewById(R.id.rv_eventAgendaPanitia);
        }
    }
}
