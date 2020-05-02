package com.example.digimiceconferent.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pending implements Parcelable {
    String id;
    String status;
    String name;
    String price;
    String maxParticipant;
    String pending;
    String url;
    String nameEvent;

    public Pending() {
    }

    protected Pending(Parcel in) {
        id = in.readString();
        status = in.readString();
        name = in.readString();
        price = in.readString();
        maxParticipant = in.readString();
        pending = in.readString();
        url = in.readString();
        nameEvent = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(status);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(maxParticipant);
        dest.writeString(pending);
        dest.writeString(url);
        dest.writeString(nameEvent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pending> CREATOR = new Creator<Pending>() {
        @Override
        public Pending createFromParcel(Parcel in) {
            return new Pending(in);
        }

        @Override
        public Pending[] newArray(int size) {
            return new Pending[size];
        }
    };

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getPending() {
        return pending;
    }

    public void setPending(String expired) {
        this.pending = expired;
    }

}
