package com.example.tugasbesar.foodreservationdelivery.models;

/**
 * Created by uber on 12/12/16.
 */

public class CartItem {
    private String id_produk;
    private String nama_produk;
    private String harga_produk;
    private String gambar_produk;
    private String qty;
    private String subtotal;
    private String status_pesan;
    private String status;

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getGambar_produk() {
        return gambar_produk;
    }

    public void setGambar_produk(String gambar_produk) {
        this.gambar_produk = gambar_produk;
    }

    public String getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(String harga_produk) {
        this.harga_produk = harga_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }
    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_pesan() {
        return status_pesan;
    }

    public void setStatus_pesan(String status_pesan) {
        this.status_pesan = status_pesan;
    }
}
