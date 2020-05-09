package com.example.digimiceconferent.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Activity.EditAgenda;
import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.MyUrl;
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
    public void onBindViewHolder(@NonNull final AgendaHolder holder, int position) {
        final Agenda agenda = list.get(position);
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
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                Agenda agenda = list.get(holder.getAdapterPosition());
                Intent intent = new Intent(holder.itemView.getContext(), EditAgenda.class);
                intent.putExtra(EditAgenda.EXTRA_EDIT_AGENDA, agenda);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Peringatan");
                builder.setMessage("Hapus agenda " + agenda.getNamaAgenda()+" ?\nMenghapus agenda akan menghapus data materi agenda");
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.delete(agenda.getId(),agenda);
                    }
                });
                builder.setNegativeButton("Tidak", null);
                builder.show();
                return true;
            }
        });
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

        private void delete(String agendaId, final Agenda agenda) {
            RequestQueue queue = Volley.newRequestQueue(itemView.getContext());
            String url = MyUrl.URL+"/delete-agenda/"+agendaId;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    notifyItemRemoved(getAdapterPosition());
                    list.remove(agenda);
                    Toast.makeText(itemView.getContext(), "Berhasil Hapus", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(itemView.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.getCache().clear();
            queue.add(request);
        }
    }


}
