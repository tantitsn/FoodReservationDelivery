package com.example.tugasbesar.foodreservationdelivery.interfaces;

import com.example.tugasbesar.foodreservationdelivery.api.JSONMenu;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

/**
 * Created by uber on 12/12/16.
 */

public interface MenuAPI {
    @Headers("Cache-Control: no-cache")
    @GET("fdr/API/get_produkKategori.php")
    Call<JSONMenu> getMenu(
            @QueryMap Map<String, String> options
            );
}
