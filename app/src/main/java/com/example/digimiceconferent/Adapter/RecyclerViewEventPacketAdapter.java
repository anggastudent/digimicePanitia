package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.EventPacket;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewEventPacketAdapter extends RecyclerView.Adapter<RecyclerViewEventPacketAdapter.EventPacketHolder> {
    ArrayList<EventPacket> listPacket = new ArrayList<>();

    public void sendDataPacket(ArrayList<EventPacket> eventPackets) {
        listPacket.clear();
        listPacket.addAll(eventPackets);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public EventPacketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_packet, parent, false);
        return new EventPacketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventPacketHolder holder, int position) {
        EventPacket eventPacket = listPacket.get(position);
        holder.name_packet.setText(eventPacket.getName_packet());
        holder.max_participant.setText(eventPacket.getMax_participant());
        holder.price.setText(eventPacket.getPrice());
    }

    @Override
    public int getItemCount() {
        return listPacket.size();
    }

    public class EventPacketHolder extends RecyclerView.ViewHolder {

        TextView name_packet, max_participant,price;
        public EventPacketHolder(@NonNull View itemView) {
            super(itemView);
            name_packet = itemView.findViewById(R.id.name_paket);
            max_participant = itemView.findViewById(R.id.max_participant);
            price = itemView.findViewById(R.id.price_packet);
        }
    }
}
