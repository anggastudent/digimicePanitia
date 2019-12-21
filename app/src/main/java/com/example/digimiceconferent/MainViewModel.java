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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<EventPanitia>> listEventPanitia = new MutableLiveData<>();
    private MutableLiveData<ArrayList<EventSessionPanitia>> listEventSessionPanitia = new MutableLiveData<>();
    private Context context;

    public MainViewModel() {
    }

    public void setEventPanitia(RequestQueue queue, final Context context) {
        final ArrayList<EventPanitia> listItemEventPanitia = new ArrayList<>();
        String url = "http://192.168.55.7/myAPI/public/event";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        EventPanitia eventPanitia = new EventPanitia();
                        eventPanitia.setJudul(data.getString("name"));
                        eventPanitia.setStart(data.getString("start"));
                        eventPanitia.setEnd(data.getString("end"));
                        listItemEventPanitia.add(eventPanitia);
                    }

                    listEventPanitia.postValue(listItemEventPanitia);

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

        String url = "http://192.168.55.7/myAPI/public/session-agenda/?id_event_session="+event_session;

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
        String url = "http://192.168.55.7/myAPI/public/event-agenda?id_event_session=1&id_event=1";

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

    LiveData<ArrayList<EventPanitia>> getEventPanitia() {
        return listEventPanitia;
    }

    LiveData<ArrayList<EventSessionPanitia>> getEventSessionPanitia() {
        return listEventSessionPanitia;
    }


}
