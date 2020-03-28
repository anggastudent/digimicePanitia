package com.example.digimiceconferent.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAgendaAdapter extends RecyclerView.Adapter<RecyclerViewAgendaAdapter.AgendaHolder> {
    ArrayList<Agenda> list = new ArrayList<>();

    public void sendDataAgenda(ArrayList<Agenda> agenda) {
        list.clear();
        list.addAll(agenda);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AgendaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda, parent, false);
        return new AgendaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaHolder holder, int position) {
        Agenda agenda = list.get(position);
        holder.tvSessionAgenda.setText(agenda.getSessionAgenda());
        holder.tvNameAgenda.setText(agenda.getNamaAgenda());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateStart = dateFormat.parse(agenda.getStartAgenda());
            Date dateEnd = dateFormat.parse(agenda.getEndAgenda());
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy");
            holder.tvWaktuAgenda.setText(dateFormatNew.format(dateStart)+" - "+dateFormatNew.format(dateEnd));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvDescAgenda.setText(agenda.getDescAgenda());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AgendaHolder extends RecyclerView.ViewHolder {
        TextView tvSessionAgenda, tvNameAgenda, tvWaktuAgenda, tvDescAgenda;

        public AgendaHolder(@NonNull View itemView) {
            super(itemView);
            tvSessionAgenda = itemView.findViewById(R.id.sesion_agenda);
            tvNameAgenda = itemView.findViewById(R.id.name_agenda);
            tvWaktuAgenda = itemView.findViewById(R.id.waktu_agenda);
            tvDescAgenda = itemView.findViewById(R.id.description_agenda);
        }
    }
}
