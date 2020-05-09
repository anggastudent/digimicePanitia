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
import com.example.digimiceconferent.Activity.EditSession;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.MyUrl;
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
            private long lastClick = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1000) {
                    return;
                }
                lastClick = SystemClock.elapsedRealtime();

                Intent intent = new Intent(holder.itemView.getContext(), EditSession.class);
                intent.putExtra(EditSession.EXTRA_EDIT_SESSION, eventSession);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (list.size() == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setMessage("Tidak dapat menghapus sesi");
                    builder.setPositiveButton("Ok", null);
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("Peringatan");
                    builder.setMessage("Hapus sesi " + eventSession.getJudul()+" ?\nMenghapus sesi akan menghapus data agenda sesi");
                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            holder.delete(eventSession.getId(),eventSession);
                        }
                    });
                    builder.setNegativeButton("Tidak", null);
                    builder.show();
                }

                return true;
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

        private void delete(String sessionId, final EventSession eventSession) {
            RequestQueue queue = Volley.newRequestQueue(itemView.getContext());
            String url = MyUrl.URL+"/delete-session/"+sessionId;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    notifyItemRemoved(getAdapterPosition());
                    list.remove(eventSession);
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
