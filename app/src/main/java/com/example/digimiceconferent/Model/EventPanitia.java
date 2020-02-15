package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class EventPanitia implements Parcelable {

    String judul;
    String start;
    String end;

    public EventPanitia() {
    }

    protected EventPanitia(Parcel in) {
        judul = in.readString();
        start = in.readString();
        end = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(judul);
        dest.writeString(start);
        dest.writeString(end);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventPanitia> CREATOR = new Creator<EventPanitia>() {
        @Override
        public EventPanitia createFromParcel(Parcel in) {
            return new EventPanitia(in);
        }

        @Override
        public EventPanitia[] newArray(int size) {
            return new EventPanitia[size];
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
