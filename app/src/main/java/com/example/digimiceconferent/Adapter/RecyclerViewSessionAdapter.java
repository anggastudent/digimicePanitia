package com.example.digimiceconferent.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Activity.EditSession;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewSessionAdapter extends RecyclerView.Adapter<RecyclerViewSessionAdapter.SessionViewHolder> {
    ArrayList<EventSession> list = new ArrayList<>();

    public void sendData(ArrayList<EventSession> sessions) {
        list.clear();
        list.addAll(sessions);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SessionViewHolder holder, int position) {
        final EventSession eventSession = list.get(position);
        holder.namaSesi.setText(eventSession.getJudul());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), EditSession.class);
                intent.putExtra(EditSession.EXTRA_EDIT_SESSION, eventSession);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView namaSesi;
        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            namaSesi = itemView.findViewById(R.id.name_session);
        }
    }
}
