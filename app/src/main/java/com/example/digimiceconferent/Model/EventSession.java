package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EventSession implements Parcelable {

    private String judul;
    private ArrayList<EventAgenda> listAgenda;

    public ArrayList<EventAgenda> getListAgenda() {
        return listAgenda;
    }

    public void setListAgenda(ArrayList<EventAgenda> listAgenda) {
        this.listAgenda = listAgenda;
    }

    public EventSession() {
    }

    protected EventSession(Parcel in) {
        judul = in.readString();
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

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(judul);
    }
}
