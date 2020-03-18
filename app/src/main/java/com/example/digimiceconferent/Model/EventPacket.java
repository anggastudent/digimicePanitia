package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class EventPacket implements Parcelable {
    String id;
    String name_packet;
    String max_participant;
    String price;



    protected EventPacket(Parcel in) {
        id = in.readString();
        name_packet = in.readString();
        max_participant = in.readString();
        price = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name_packet);
        dest.writeString(max_participant);
        dest.writeString(price);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventPacket() {
    }

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

}
