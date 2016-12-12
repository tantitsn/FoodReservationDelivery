package com.example.tugasbesar.foodreservationdelivery;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tugasbesar.foodreservationdelivery.adapter.MenuItemAdapter;
import com.example.tugasbesar.foodreservationdelivery.api.JSONMenu;
import com.example.tugasbesar.foodreservationdelivery.configs.InternetConnection;
import com.example.tugasbesar.foodreservationdelivery.configs.Konfigurasi;
import com.example.tugasbesar.foodreservationdelivery.interfaces.MenuAPI;
import com.example.tugasbesar.foodreservationdelivery.models.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by uber on 12/12/16.
 */

public class MenuActivity extends AppCompatActivity {
    @BindView(R.id.grid) GridView mGridView;
    @BindView(R.id.parent) CoordinatorLayout _parent;
    private ProgressDialog pDialog;
    private MenuItemAdapter adapter;
    private ArrayList<MenuItem> mGridData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_menu);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        loadJSON();
    }

    private void loadJSON() {
        if(InternetConnection.checkConnection(getApplicationContext())){

            String id_kategori = getIntent().getStringExtra("id");
            Map<String, String> datas = new HashMap<>();
            datas.put("id_kategoti", id_kategori);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Konfigurasi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MenuAPI menuAPI = retrofit.create(MenuAPI.class);
            Call<JSONMenu> call = menuAPI.getMenu(datas);

            call.enqueue(new Callback<JSONMenu>() {
                @Override
                public void onResponse(Call<JSONMenu> call, Response<JSONMenu> response) {
                    if(response.isSuccessful()){
                        JSONMenu jsonMenu = response.body();
                        mGridData = new ArrayList<>(Arrays.asList(jsonMenu.getPosts()));
                        adapter = new MenuItemAdapter(MenuActivity.this, R.layout.grid_item_menu, mGridData);
                        mGridView.setAdapter(adapter);

                        adapter.setGridData(mGridData);
                    }else{
                        Toast.makeText(MenuActivity.this, "GAGAL!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONMenu> call, Throwable t) {
                    Toast.makeText(MenuActivity.this, "GAGAL!", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(MenuActivity.this, "Kesalahan Jaringan!", Toast.LENGTH_SHORT).show();
        }
    }
}
