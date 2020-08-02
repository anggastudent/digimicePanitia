package com.example.digimiceconferent.Adapter;

import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Activity.ScanQRPeserta;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewEventSessionAdapter extends RecyclerView.Adapter<RecyclerViewEventSessionAdapter.SessionPanitiaViewHolder> {
    ArrayList<EventSession> listSession = new ArrayList<>();

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
    public void onBindViewHolder(@NonNull final SessionPanitiaViewHolder holder, int position) {
        final EventSession eventSession = listSession.get(position);
        holder.nameSession.setText(eventSession.getJudul());

        if (eventSession.getListAgenda().size() == 0) {
            holder.btScan.setEnabled(false);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateStart = dateFormat.parse(eventSession.getStart());
            Date dateEnd = dateFormat.parse(eventSession.getEnd());
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy");
            holder.dateSession.setText(dateFormatNew.format(dateStart));

            Date date = new Date();
            String dateNow = dateFormat.format(date);
            Date getDateNow = dateFormat.parse(dateNow);

            if (getDateNow.after(dateEnd)) {
                holder.btScan.setEnabled(false);

            } else {
                holder.btScan.setEnabled(true);

            }



        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.agendaPanitiaAdapter.sendDataAgenda(eventSession.getListAgenda());

        holder.btScan.setOnClickListener(new View.OnClickListener() {
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                SharedPrefManager sharedPrefManager = new SharedPrefManager(holder.itemView.getContext());
                sharedPrefManager.saveSPString(sharedPrefManager.SP_ID_SESSION, eventSession.getId());
                Intent intent = new Intent(holder.itemView.getContext(), ScanQRPeserta.class);
                holder.itemView.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listSession.size();
    }

    public class SessionPanitiaViewHolder extends RecyclerView.ViewHolder {
        TextView nameSession, dateSession;
        Button btScan;
        RecyclerView rvagenda;
        RecyclerViewSessionAgendaAdapter agendaPanitiaAdapter;

        public SessionPanitiaViewHolder(@NonNull View itemView) {
            super(itemView);
            dateSession = itemView.findViewById(R.id.item_start_session);
            nameSession = itemView.findViewById(R.id.item_name_session);
            rvagenda = itemView.findViewById(R.id.rv_session_agenda);
            btScan = itemView.findViewById(R.id.bt_scan);
            agendaPanitiaAdapter = new RecyclerViewSessionAgendaAdapter();
            rvagenda.setAdapter(agendaPanitiaAdapter);
            rvagenda.setHasFixedSize(true);
            rvagenda.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            rvagenda.setNestedScrollingEnabled(false);
        }

    }
}
