package com.example.tugasbesar.foodreservationdelivery.interfaces;

import android.telecom.Call;

import com.example.tugasbesar.foodreservationdelivery.api.JSONKategori;

import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by tanti on 12/12/16.
 */
public interface KategoriAPI {
   @Headers("Cache-Control: no-cache")
   @GET("fdr/API/get_kategori.php")
    retrofit2.Call<JSONKategori> getKategori();

}
