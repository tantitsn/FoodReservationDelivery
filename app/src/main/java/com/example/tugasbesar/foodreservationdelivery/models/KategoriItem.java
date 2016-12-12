package com.example.tugasbesar.foodreservationdelivery.models;

/**
 * Created by tanti on 12/12/16.
 */
public class KategoriItem {
    private String id_kategori;
    private String nama_kategori;
    private String gambar_kategori;

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getGambar_kategori() {
        return gambar_kategori;
    }

    public void setGambar_kategori(String gambar_kategori) {
        this.gambar_kategori = gambar_kategori;
    }
}
