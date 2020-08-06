package com.example.digimiceconferent.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Model.Materi;
import com.example.digimiceconferent.MyUrl;
import com.example.digimiceconferent.R;
import com.example.digimiceconferent.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewMateriAdapter extends RecyclerView.Adapter<RecyclerViewMateriAdapter.MateriViewHolder> {

    ArrayList<Materi> list = new ArrayList<>();

    public void sendData(ArrayList<Materi> materi) {
        list.clear();
        list.addAll(materi);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materi, parent, false);
        return new MateriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MateriViewHolder holder, int position) {
        final Materi materi = list.get(position);
        holder.namaMateri.setText(materi.getNamaMateri());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MyUrl.URL+"/"+materi.getUrl()));
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Hapus materi " + materi.getNamaMateri()+" ?");
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.delete(materi.getId(),materi);
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

    public class MateriViewHolder extends RecyclerView.ViewHolder {
        TextView namaMateri;
        public MateriViewHolder(@NonNull View itemView) {
            super(itemView);
            namaMateri = itemView.findViewById(R.id.name_materi);
        }

        private void delete(String id, final Materi materi) {
            final SharedPrefManager sharedPrefManager = new SharedPrefManager(itemView.getContext());
            RequestQueue queue = Volley.newRequestQueue(itemView.getContext());
            String url = MyUrl.URL+"/delete-materi/"+id;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    notifyItemRemoved(getAdapterPosition());
                    list.remove(materi);
                    Toast.makeText(itemView.getContext(), "Berhasil Hapus", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(itemView.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("token", sharedPrefManager.getSPToken());
                    return data;
                }
            };
            queue.getCache().clear();
            queue.add(request);
        }
    }


}
