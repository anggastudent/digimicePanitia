package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    String id;
    String judul;
    String start;
    String end;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event() {
    }

    protected Event(Parcel in) {
        id = in.readString();
        judul = in.readString();
        start = in.readString();
        end = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(judul);
        dest.writeString(start);
        dest.writeString(end);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
