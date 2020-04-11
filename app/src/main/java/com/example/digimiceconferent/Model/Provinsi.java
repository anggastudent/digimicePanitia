package com.example.digimiceconferent.Model;

import java.util.ArrayList;

public class Provinsi {
    String id;
    String name;
    ArrayList<Kabupaten> listKabupaten;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Kabupaten> getListKabupaten() {
        return listKabupaten;
    }

    public void setListKabupaten(ArrayList<Kabupaten> listKabupaten) {
        this.listKabupaten = listKabupaten;
    }
}
