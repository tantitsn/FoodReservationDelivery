package com.example.tugasbesar.foodreservationdelivery.configs;

/**
 * Created by tanti on 12/12/16.
 */
public class Konfigurasi {
    public  static final String TAG_SUKSES="sukses";
    public  static final String TAG_PESAN="pesan";

    public static final String URL ="http://192.168.43.172";
    public static final String URL_API ="http://192.168.43.172/fdr/API/";
    public static final String GET_KATEGORI = URL_API + "get_kategori.php";
    public static final String GET_DETAIL_MENU = URL_API + "get_detail_menu.php";
    public static final String SAVE_TEMPORARY = URL_API + "save_temp_pesanan.php";
    public static final String SAVE_TRANSAKSI = URL_API + "save_transaksi.php";


    public static final String DELETE_TEMP = URL_API + "delete_temp.php";
    public static final String GET_TOTAL_CART = URL_API + "get_cart.php";
    public static final String SAVE_PESANAN = URL_API + "save_pesanan.php";
    public static final String BROADCAST_URL = URL_API + "broadcast_message.php";
    public static final String GET_MEJA = URL_API + "get_meja.php";
    public static final String TOKEN_REGISTER_URL = URL_API + "register_token.php";
}
