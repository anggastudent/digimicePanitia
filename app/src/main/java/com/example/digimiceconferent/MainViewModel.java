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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.digimiceconferent.Model.Agenda;
import com.example.digimiceconferent.Model.Event;
import com.example.digimiceconferent.Model.Expired;
import com.example.digimiceconferent.Model.Kabupaten;
import com.example.digimiceconferent.Model.Paid;
import com.example.digimiceconferent.Model.Pending;
import com.example.digimiceconferent.Model.Provinsi;
import com.example.digimiceconferent.Model.Rekapitulasi;
import com.example.digimiceconferent.Model.SessionAgenda;
import com.example.digimiceconferent.Model.EventPacket;
import com.example.digimiceconferent.Model.EventSession;
import com.example.digimiceconferent.Model.Materi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Event>> listEventPanitia = new MutableLiveData<>();
    private MutableLiveData<ArrayList<EventSession>> listEventSessionPanitia = new MutableLiveData<>();
    private MutableLiveData<ArrayList<EventPacket>> listPacket = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Agenda>> listAgenda = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Materi>> listMateri = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Provinsi>> listProvinsi = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Rekapitulasi>> listRekapitulasi = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Pending>> listPending = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Expired>> listExpired = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Paid>> listPaid = new MutableLiveData<>();

    public MainViewModel() {
    }


    public void setEventPanitia(RequestQueue queue, final Context context, String user_id) {
        final ArrayList<Event> listItemEvent = new ArrayList<>();

        String url = "http://192.168.4.109/myAPI/public/event?user_id="+user_id;
       JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
           @Override
           public void onResponse(JSONArray response) {
               try {
                   for (int i = 0; i < response.length(); i++) {
                       JSONObject data = response.getJSONObject(i);
                       Event event = new Event();
                       event.setId(data.getString("id"));
                       event.setJudul(data.getString("name"));
                       event.setStart(data.getString("start"));
                       event.setEnd(data.getString("end"));
                       event.setPlace(data.getString("place"));
                       event.setAddress(data.getString("address"));
                       event.setBanner(data.getString("banner"));
                       event.setPresenceType(data.getString("presence_type"));
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

    public void setListEventSessionPanitia(final RequestQueue queue, final Context context, String eventId) {
        final ArrayList<EventSession> listItemEventSession = new ArrayList<>();
        

        String url = "http://192.168.4.109/myAPI/public/session/"+eventId;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        EventSession eventSession = new EventSession();
                        eventSession.setId(data.getString("id"));
                        eventSession.setJudul(data.getString("name"));
                        String start = "";
                        String end = "";

                        JSONArray dataAgenda = data.getJSONArray("agenda");
                        ArrayList<SessionAgenda> listAgenda = new ArrayList<>();
                        for (int j = 0; j < dataAgenda.length(); j++) {
                            SessionAgenda agenda = new SessionAgenda();
                            JSONObject data2 = dataAgenda.getJSONObject(j);
                            agenda.setJudul(data2.getString("name"));
                            agenda.setJam(data2.getString("start"));
                            agenda.setDesc(data2.getString("description"));
                            if (j == 0) {
                                start+=data2.getString("start");
                            }
                            if(j==dataAgenda.length()-1){
                                end+=data2.getString("end");
                            }
                            listAgenda.add(agenda);
                        }
                        eventSession.setEnd(end);
                        eventSession.setStart(start);
                        eventSession.setListAgenda(listAgenda);
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


    public void setListPacket(RequestQueue queue, final Context context) {
        final ArrayList<EventPacket> list = new ArrayList<>();
        String url = "http://192.168.4.109/myAPI/public/paket";


        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        EventPacket eventPacket = new EventPacket();
                        eventPacket.setId(data.getString("id"));
                        eventPacket.setName_packet(data.getString("name"));
                        eventPacket.setMax_participant(data.getString("max_participant"));
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

    public void setListAgenda(RequestQueue queue, final Context context, String id_event) {

        final ArrayList<Agenda> list = new ArrayList<>();
        String url = "http://192.168.4.109/myAPI/public/agenda?event_id="+id_event;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        Agenda agenda = new Agenda();
                        agenda.setId(data.getString("id"));
                        agenda.setIdSession(data.getString("session_id"));
                        agenda.setNamaAgenda(data.getString("name"));
                        agenda.setDescAgenda(data.getString("description"));
                        agenda.setStartAgenda(data.getString("start"));
                        agenda.setEndAgenda(data.getString("end"));
                        agenda.setSessionAgenda(data.getString("session"));
                        list.add(agenda);
                    }

                    listAgenda.postValue(list);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(arrayRequest);
    }

    public void setListMateri(RequestQueue queue, final Context context, String idAgenda) {
        final ArrayList<Materi> list = new ArrayList<>();
        String url = "http://192.168.4.109/myAPI/public/materi/"+idAgenda;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        Materi materi = new Materi();
                        materi.setNamaMateri(data.getString("name"));
                        list.add(materi);
                    }

                    listMateri.postValue(list);
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

    public void setListProvinsi(RequestQueue queue, final Context context) {
        final ArrayList<Provinsi> list = new ArrayList<>();
        String url = "http://192.168.4.109/myAPI/public/provinsi";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        Provinsi provinsi = new Provinsi();
                        provinsi.setId(data.getString("id"));
                        provinsi.setName(data.getString("name"));
                        JSONArray dataKabupaten = data.getJSONArray("kabupaten");
                        ArrayList<Kabupaten> listKabupaten = new ArrayList<>();
                        for (int j = 0; j < dataKabupaten.length(); j++) {
                            JSONObject data2 = dataKabupaten.getJSONObject(j);
                            Kabupaten kabupaten = new Kabupaten();
                            kabupaten.setId(data2.getString("id"));
                            kabupaten.setName(data2.getString("name"));
                            listKabupaten.add(kabupaten);
                        }
                        provinsi.setListKabupaten(listKabupaten);
                        list.add(provinsi);

                    }

                    listProvinsi.postValue(list);
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

    public void setListRekapitulasi(RequestQueue queue, final Context context, String eventId, String sessionId) {
        final ArrayList<Rekapitulasi> list = new ArrayList<>();
        String url = "http://192.168.4.109/myAPI/public/rekapitulasi?event_id="+eventId+"&session_id="+sessionId;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        Rekapitulasi rekapitulasi = new Rekapitulasi();
                        rekapitulasi.setName(data.getString("name"));
                        rekapitulasi.setEmail(data.getString("email"));
                        rekapitulasi.setPhone(data.getString("phone"));
                        rekapitulasi.setRekap(data.getString("rekap"));
                        rekapitulasi.setPaymentStatus(data.getString("payment_status"));
                        rekapitulasi.setProvinsi(data.getString("provinsi"));
                        list.add(rekapitulasi);
                    }

                    listRekapitulasi.postValue(list);
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

    public void setListPending(RequestQueue queue, final Context context, String userId) {
        final ArrayList<Pending> list = new ArrayList<>();
        String url = "http://192.168.4.109/myAPI/public/pending/" + userId;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        Pending pending = new Pending();
                        pending.setId(data.getString("id_invoice"));
                        pending.setName(data.getString("name_paket"));
                        pending.setStatus(data.getString("status"));
                        pending.setMaxParticipant(data.getString("max_participant"));
                        pending.setPrice(data.getString("price"));
                        pending.setExpired(data.getString("expired"));
                        list.add(pending);
                    }
                    listPending.postValue(list);
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

    public void setListExpired(RequestQueue queue, final Context context, String userId) {
        final ArrayList<Expired> list = new ArrayList<>();
        String url = "http://192.168.4.109/myAPI/public/expired/" + userId;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        Expired expired = new Expired();
                        expired.setId(data.getString("id_invoice"));
                        expired.setName(data.getString("name_paket"));
                        expired.setMaxParticipant("max_participant");
                        expired.setStatus(data.getString("status"));
                        expired.setPrice(data.getString("price"));
                        expired.setExpired(data.getString("expired"));
                        list.add(expired);
                    }

                    listExpired.postValue(list);
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

    public void setListPaid(RequestQueue queue, final Context context, String userId) {
        final ArrayList<Paid> list = new ArrayList<>();
        String url = "http://192.168.4.109/myAPI/public/paid/" + userId;
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        Paid paid = new Paid();
                        paid.setId(data.getString("id_invoice"));
                        paid.setStatus(data.getString("status"));
                        paid.setName(data.getString("name_paket"));
                        paid.setPrice(data.getString("price"));
                        paid.setMaxParticipant(data.getString("max_participant"));
                        paid.setPaid(data.getString("paid_at"));
                        list.add(paid);
                    }

                    listPaid.postValue(list);
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
    public LiveData<ArrayList<EventPacket>> getEventPacket() {
        return listPacket;
    }

    public LiveData<ArrayList<Event>> getEventPanitia() {
        return listEventPanitia;
    }

    public LiveData<ArrayList<EventSession>> getEventSessionPanitia() {
        return listEventSessionPanitia;
    }

    public LiveData<ArrayList<Agenda>> getAgenda() {
        return listAgenda;
    }

    public LiveData<ArrayList<Materi>> getMateri() {
        return listMateri;
    }

    public LiveData<ArrayList<Provinsi>> getProvinsi() {
        return listProvinsi;
    }

    public LiveData<ArrayList<Rekapitulasi>> getRekapitulasi() {
        return listRekapitulasi;
    }

    public LiveData<ArrayList<Pending>> getPending() {
        return listPending;
    }

    public LiveData<ArrayList<Expired>> getExpired() {
        return listExpired;
    }

    public LiveData<ArrayList<Paid>> getPaid() {
        return listPaid;
    }




}
