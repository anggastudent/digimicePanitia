package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class EventPacket implements Parcelable {
    String name_packet;
    String max_participant;
    String price;

    public EventPacket() {
    }

    protected EventPacket(Parcel in) {
        name_packet = in.readString();
        max_participant = in.readString();
        price = in.readString();
    }

    public static final Creator<EventPacket> CREATOR = new Creator<EventPacket>() {
        @Override
        public EventPacket createFromParcel(Parcel in) {
            return new EventPacket(in);
        }

        @Override
        public EventPacket[] newArray(int size) {
            return new EventPacket[size];
        }
    };

    public String getName_packet() {
        return name_packet;
    }

    public void setName_packet(String name_packet) {
        this.name_packet = name_packet;
    }

    public String getMax_participant() {
        return max_participant;
    }

    public void setMax_participant(String max_participant) {
        this.max_participant = max_participant;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name_packet);
        dest.writeString(max_participant);
        dest.writeString(price);
    }
}
