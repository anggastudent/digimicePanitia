package com.example.digimiceconferent.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.digimiceconferent.Activity.DetailEventPanitia;
import com.example.digimiceconferent.Model.EventPresensi;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewEventPresensiAdapter extends RecyclerView.Adapter<RecyclerViewEventPresensiAdapter.EventPanitiaViewHolder> {

    ArrayList<EventPresensi> listEvent = new ArrayList<>();

    public void sendEventPanitia(ArrayList<EventPresensi> eventPresensis) {
        listEvent.clear();
        listEvent.addAll(eventPresensis);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventPanitiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_panitia, parent, false);
        return new EventPanitiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventPanitiaViewHolder holder, int position) {
        final EventPresensi eventPresensi = listEvent.get(position);
        holder.judul.setText(eventPresensi.getJudul());
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventPresensi eventPresensi = listEvent.get(holder.getAdapterPosition());
                Intent intent = new Intent(holder.itemView.getContext(), DetailEventPanitia.class);
                intent.putExtra(DetailEventPanitia.EXTRA_EVENT_PANITIA, eventPresensi);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

    public class EventPanitiaViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView judul;
        Button btnDetail;

        public EventPanitiaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item);
            judul = itemView.findViewById(R.id.judul_item);
            btnDetail = itemView.findViewById(R.id.bt_detail_item);
        }
    }
}
