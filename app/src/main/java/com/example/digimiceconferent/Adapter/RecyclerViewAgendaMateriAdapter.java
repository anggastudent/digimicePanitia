package com.example.digimiceconferent.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digimiceconferent.Activity.DetailUploadMateri;
import com.example.digimiceconferent.Activity.EditAgenda;
import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAgendaMateriAdapter extends RecyclerView.Adapter<RecyclerViewAgendaMateriAdapter.AgendaMateriViewHolder> {
    ArrayList<Agenda> list = new ArrayList<>();

    public void sendData(ArrayList<Agenda> agenda) {
        list.clear();
        list.addAll(agenda);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AgendaMateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda, parent, false);
        return new AgendaMateriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AgendaMateriViewHolder holder, int position) {
        Agenda agenda = list.get(position);
        holder.tvSessionAgenda.setText(agenda.getSessionAgenda());
        holder.tvNameAgenda.setText(agenda.getNamaAgenda());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateStart = dateFormat.parse(agenda.getStartAgenda());
            Date dateEnd = dateFormat.parse(agenda.getEndAgenda());
            SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            holder.tvWaktuAgenda.setText(dateFormatNew.format(dateStart)+" - "+dateFormatNew.format(dateEnd));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvDescAgenda.setText(agenda.getDescAgenda());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Agenda agenda = list.get(holder.getAdapterPosition());
                Intent intent = new Intent(holder.itemView.getContext(), DetailUploadMateri.class);
                intent.putExtra(DetailUploadMateri.EXTRA_MATERI, agenda);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AgendaMateriViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionAgenda, tvNameAgenda, tvWaktuAgenda, tvDescAgenda;
        public AgendaMateriViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSessionAgenda = itemView.findViewById(R.id.sesion_agenda);
            tvNameAgenda = itemView.findViewById(R.id.name_agenda);
            tvWaktuAgenda = itemView.findViewById(R.id.waktu_agenda);
            tvDescAgenda = itemView.findViewById(R.id.description_agenda);
        }
    }
}
