package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Expired implements Parcelable {
    String id;
    String status;
    String name;
    String nameEvent;
    String price;
    String maxParticipant;
    String expired;
    String url;

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    protected Expired(Parcel in) {
        id = in.readString();
        status = in.readString();
        name = in.readString();
        nameEvent = in.readString();
        price = in.readString();
        maxParticipant = in.readString();
        expired = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(status);
        dest.writeString(name);
        dest.writeString(nameEvent);
        dest.writeString(price);
        dest.writeString(maxParticipant);
        dest.writeString(expired);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Expired> CREATOR = new Creator<Expired>() {
        @Override
        public Expired createFromParcel(Parcel in) {
            return new Expired(in);
        }

        @Override
        public Expired[] newArray(int size) {
            return new Expired[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Expired() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMaxParticipant() {
        return maxParticipant;
    }

    public void setMaxParticipant(String maxParticipant) {
        this.maxParticipant = maxParticipant;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

}
