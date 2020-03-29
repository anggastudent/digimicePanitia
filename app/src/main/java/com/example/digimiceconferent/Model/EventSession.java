package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EventSession implements Parcelable {

    private String judul;
    private String id;
    private ArrayList<EventAgenda> listAgenda;

    protected EventSession(Parcel in) {
        judul = in.readString();
        id = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(judul);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventSession> CREATOR = new Creator<EventSession>() {
        @Override
        public EventSession createFromParcel(Parcel in) {
            return new EventSession(in);
        }

        @Override
        public EventSession[] newArray(int size) {
            return new EventSession[size];
        }
    };

    public ArrayList<EventAgenda> getListAgenda() {
        return listAgenda;
    }

    public void setListAgenda(ArrayList<EventAgenda> listAgenda) {
        this.listAgenda = listAgenda;
    }

    public EventSession() {
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

}
