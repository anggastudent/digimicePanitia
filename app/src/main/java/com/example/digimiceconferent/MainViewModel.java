package com.example.digimiceconferent;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.digimiceconferent.Model.EventAgendaPanitia;
import com.example.digimiceconferent.Model.EventPresensi;
import com.example.digimiceconferent.Model.EventSessionPanitia;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<EventPresensi>> listEventPanitia = new MutableLiveData<>();
    private MutableLiveData<ArrayList<EventSessionPanitia>> listEventSessionPanitia = new MutableLiveData<>();
    private Context context;

    public MainViewModel() {
    }

    public void setEventPanitia(RequestQueue queue, final Context context) {
        final ArrayList<EventPresensi> listItemEventPresensi = new ArrayList<>();
        String url = "http://192.168.43.192/myAPI/public/event";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        EventPresensi eventPresensi = new EventPresensi();
                        eventPresensi.setJudul(data.getString("name"));
                        eventPresensi.setStart(data.getString("start"));
                        eventPresensi.setEnd(data.getString("end"));
                        listItemEventPresensi.add(eventPresensi);
                    }

                    listEventPanitia.postValue(listItemEventPresensi);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(arrayRequest);
    }

    public void setListEventSessionPanitia(final RequestQueue queue, final Context context, String event_session) {
        final ArrayList<EventSessionPanitia> listItemEventSession = new ArrayList<>();

        String url = "http://192.168.43.192/myAPI/public/session-agenda/?id_event_session="+event_session;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        EventSessionPanitia eventSessionPanitia = new EventSessionPanitia();
                        eventSessionPanitia.setJudul(data.getString("name"));
                        eventSessionPanitia.setListAgenda(setEventAgendaPanitia(queue,context));
                        listItemEventSession.add(eventSessionPanitia);
                    }

                    listEventSessionPanitia.postValue(listItemEventSession);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(arrayRequest);
    }

    public ArrayList<EventAgendaPanitia> setEventAgendaPanitia(RequestQueue queue, final Context context) {
        final ArrayList<EventAgendaPanitia> listItemAgenda = new ArrayList<>();
        String url = "http://192.168.43.192/myAPI/public/event-agenda?id_event_session=1&id_event=1";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        EventAgendaPanitia eventAgendaPanitia = new EventAgendaPanitia();
                        eventAgendaPanitia.setJudul(data.getString("name"));
                        eventAgendaPanitia.setJam(data.getString("start"));
                        listItemAgenda.add(eventAgendaPanitia);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(arrayRequest);
        return listItemAgenda;
    }

    public LiveData<ArrayList<EventPresensi>> getEventPanitia() {
        return listEventPanitia;
    }

    public LiveData<ArrayList<EventSessionPanitia>> getEventSessionPanitia() {
        return listEventSessionPanitia;
    }





}
