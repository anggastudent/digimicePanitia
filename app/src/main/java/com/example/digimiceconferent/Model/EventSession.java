package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EventSession implements Parcelable {

    private String judul;
    private String id;
    private String start;
    private String end;

    private ArrayList<SessionAgenda> listAgenda;

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    protected EventSession(Parcel in) {
        judul = in.readString();
        id = in.readString();
        start = in.readString();
        end = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(judul);
        dest.writeString(id);
        dest.writeString(start);
        dest.writeString(end);
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<SessionAgenda> getListAgenda() {
        return listAgenda;
    }

    public void setListAgenda(ArrayList<SessionAgenda> listAgenda) {
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
