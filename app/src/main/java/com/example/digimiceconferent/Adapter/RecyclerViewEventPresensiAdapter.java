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
import com.example.digimiceconferent.Activity.KelolaPeserta;
import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewEventPresensiAdapter extends RecyclerView.Adapter<RecyclerViewEventPresensiAdapter.EventPresensiViewHolder> {
    ArrayList<Event> list = new ArrayList<>();
    SharedPrefManager sharedPrefManager;

    public void sendData(ArrayList<Event> events) {
        list.clear();
        list.addAll(events);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventPresensiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_panitia, parent, false);
        return new EventPresensiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventPresensiViewHolder holder, int position) {
        final Event event = list.get(position);
        sharedPrefManager = new SharedPrefManager(holder.itemView.getContext());

        holder.judul.setText(event.getJudul());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateStart = dateFormat.parse(event.getStart());
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy");
            holder.start.setText(dateFormatNew.format(dateStart));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(holder.itemView.getContext())
                .load("http://192.168.3.5/myAPI/public/" + event.getBanner())
                .apply(new RequestOptions().override(100, 100))
                .into(holder.banner);



        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPrefManager.saveSPString(sharedPrefManager.SP_ID_EVENT, event.getId());
                sharedPrefManager.saveSPString(sharedPrefManager.SP_NAME_EVENT, event.getJudul());
                sharedPrefManager.saveSPString(sharedPrefManager.SP_PLACE_EVENT, event.getPlace());
                sharedPrefManager.saveSPString(sharedPrefManager.SP_ADDRESS_EVENT, event.getAddress());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dateStart = dateFormat.parse(event.getStart());
                    Date dateEnd = dateFormat.parse(event.getEnd());
                    SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy");
                    sharedPrefManager.saveSPString(sharedPrefManager.SP_WAKTU_EVENT, dateFormatNew.format(dateStart)+" - "+dateFormatNew.format(dateEnd));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(holder.itemView.getContext(), KelolaPeserta.class);
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventPresensiViewHolder extends RecyclerView.ViewHolder {
        TextView judul, start;
        Button btnDetail;
        ImageView banner;

        public EventPresensiViewHolder(@NonNull View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.judul_item);
            start = itemView.findViewById(R.id.item_start);
            btnDetail = itemView.findViewById(R.id.bt_detail_item);
            banner = itemView.findViewById(R.id.img_item);
        }
    }
}
