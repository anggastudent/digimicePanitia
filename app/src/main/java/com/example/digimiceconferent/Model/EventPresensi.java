package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class EventPresensi implements Parcelable {

    String judul;
    String start;
    String end;

    public EventPresensi() {
    }

    protected EventPresensi(Parcel in) {
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

    public static final Creator<EventPresensi> CREATOR = new Creator<EventPresensi>() {
        @Override
        public EventPresensi createFromParcel(Parcel in) {
            return new EventPresensi(in);
        }

        @Override
        public EventPresensi[] newArray(int size) {
            return new EventPresensi[size];
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
