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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.Model.EventAgenda;
import com.example.digimiceconferent.Model.EventPacket;
import com.example.digimiceconferent.Model.EventSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Event>> listEventPanitia = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> listEventAddPanitia = new MutableLiveData<>();
    private MutableLiveData<ArrayList<EventSession>> listEventSessionPanitia = new MutableLiveData<>();
    private MutableLiveData<ArrayList<EventPacket>> listPacket = new MutableLiveData<>();
    private SharedPrefManager sharedPrefManager;

    public MainViewModel() {
    }

    public void setListEventAddPanitia(RequestQueue queue, final Context context, String user_id) {
        final ArrayList<String> list = new ArrayList<>();
        String url = "http://192.168.4.107/myAPI/public/event?user_id="+user_id;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i=0; i < response.length(); i++){
                        JSONObject data = response.getJSONObject(i);
                        list.add(data.getString("id"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(arrayRequest);
    }
    public void setEventPanitia(RequestQueue queue, final Context context, String user_id) {
        final ArrayList<Event> listItemEvent = new ArrayList<>();

        String url = "http://192.168.4.107/myAPI/public/event?user_id="+user_id;
       JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
           @Override
           public void onResponse(JSONArray response) {
               try {
                   for (int i = 0; i < response.length(); i++) {
                       JSONObject data = response.getJSONObject(i);
                       Event event = new Event();
                       event.setJudul(data.getString("name"));
                       event.setStart(data.getString("start"));
                       event.setEnd(data.getString("end"));
                       listItemEvent.add(event);
                   }
                   listEventPanitia.postValue(listItemEvent);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
           }
       });

        queue.add(arrayRequest);
    }

    public void setListEventSessionPanitia(final RequestQueue queue, final Context context, String event_session) {
        final ArrayList<EventSession> listItemEventSession = new ArrayList<>();

        String url = "http://192.168.4.107/myAPI/public/session-agenda/?id_event_session="+event_session;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        EventSession eventSession = new EventSession();
                        eventSession.setJudul(data.getString("name"));
                        eventSession.setListAgenda(setEventAgendaPanitia(queue,context));
                        listItemEventSession.add(eventSession);
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

    public ArrayList<EventAgenda> setEventAgendaPanitia(RequestQueue queue, final Context context) {
        final ArrayList<EventAgenda> listItemAgenda = new ArrayList<>();
        String url = "http://192.168.4.107/myAPI/public/event-agenda?id_event_session=1&id_event=1";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        EventAgenda eventAgenda = new EventAgenda();
                        eventAgenda.setJudul(data.getString("name"));
                        eventAgenda.setJam(data.getString("start"));
                        listItemAgenda.add(eventAgenda);
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

    public void setListPacket(RequestQueue queue, final Context context) {
        final ArrayList<EventPacket> list = new ArrayList<>();
        String url = "http://192.168.4.107/myAPI/public/paket";


        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        EventPacket eventPacket = new EventPacket();
                        eventPacket.setId(data.getString("id"));
                        eventPacket.setName_packet(data.getString("name"));
                        eventPacket.setMax_participant(data.getString("max_participant")+" Maksimal Peserta");
                        eventPacket.setPrice(data.getString("price"));
                        list.add(eventPacket);

                    }
                    listPacket.postValue(list);
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

    public LiveData<ArrayList<EventPacket>> getEventPacket() {
        return listPacket;
    }

    public LiveData<ArrayList<Event>> getEventPanitia() {
        return listEventPanitia;
    }

    public LiveData<ArrayList<EventSession>> getEventSessionPanitia() {
        return listEventSessionPanitia;
    }





}
