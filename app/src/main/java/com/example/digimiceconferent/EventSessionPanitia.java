package com.example.digimiceconferent;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EventSessionPanitia implements Parcelable {

    private String judul;
    private ArrayList<EventAgendaPanitia> listAgenda;

    public ArrayList<EventAgendaPanitia> getListAgenda() {
        return listAgenda;
    }

    public void setListAgenda(ArrayList<EventAgendaPanitia> listAgenda) {
        this.listAgenda = listAgenda;
    }

    public EventSessionPanitia() {
    }

    protected EventSessionPanitia(Parcel in) {
        judul = in.readString();
    }

    public static final Creator<EventSessionPanitia> CREATOR = new Creator<EventSessionPanitia>() {
        @Override
        public EventSessionPanitia createFromParcel(Parcel in) {
            return new EventSessionPanitia(in);
        }

        @Override
        public EventSessionPanitia[] newArray(int size) {
            return new EventSessionPanitia[size];
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
