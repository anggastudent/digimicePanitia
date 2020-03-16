package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.R;

import java.util.ArrayList;

public class RecyclerViewEventAdapter extends RecyclerView.Adapter<RecyclerViewEventAdapter.EventPanitiaViewHolder> {

    ArrayList<Event> listEvent = new ArrayList<>();

    public void sendEventPanitia(ArrayList<Event> events) {
        listEvent.clear();
        listEvent.addAll(events);
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
        final Event event = listEvent.get(position);
        holder.judul.setText(event.getJudul());
//        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Event event = listEvent.get(holder.getAdapterPosition());
//                Intent intent = new Intent(holder.itemView.getContext(), DetailEventPanitia.class);
//                intent.putExtra(DetailEventPanitia.EXTRA_EVENT_PANITIA, event);
//                holder.itemView.getContext().startActivity(intent);
//            }
//        });
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
