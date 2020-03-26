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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.digimiceconferent.Activity.KelolaEvent;
import com.example.digimiceconferent.Activity.KelolaPacket;
import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(event.getStart());
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMMM yyyy");
            holder.start.setText(dateFormat1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(holder.itemView.getContext())
                .load("http://192.168.4.107/myAPI/public/" + event.getBanner())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.imageView);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = listEvent.get(holder.getAdapterPosition());
                Intent intent = new Intent(holder.itemView.getContext(), KelolaEvent.class);
                intent.putExtra(KelolaEvent.EXTRA_INTENT, event);
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
        TextView judul,start;
        Button btnDetail;

        public EventPanitiaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item);
            judul = itemView.findViewById(R.id.judul_item);
            start = itemView.findViewById(R.id.item_start);
            btnDetail = itemView.findViewById(R.id.bt_detail_item);
        }
    }
}
