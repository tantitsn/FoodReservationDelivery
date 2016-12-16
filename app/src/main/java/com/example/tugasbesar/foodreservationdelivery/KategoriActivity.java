package com.example.tugasbesar.foodreservationdelivery;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tugasbesar.foodreservationdelivery.adapter.KategoriAdapter;
import com.example.tugasbesar.foodreservationdelivery.api.JSONKategori;
import com.example.tugasbesar.foodreservationdelivery.configs.InternetConnection;
import com.example.tugasbesar.foodreservationdelivery.configs.Konfigurasi;
import com.example.tugasbesar.foodreservationdelivery.configs.SessionManagement;
import com.example.tugasbesar.foodreservationdelivery.interfaces.KategoriAPI;
import com.example.tugasbesar.foodreservationdelivery.models.KategoriItem;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tanti on 12/12/16.
 */
public class KategoriActivity extends AppCompatActivity {

    @BindView(R.id.gridView) GridView mGridView;
    @BindView(R.id.parent) CoordinatorLayout _parent;
    private ProgressDialog pDialog;
    private KategoriAdapter adapter;
    private ArrayList<KategoriItem> mGridData;

    SessionManagement session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_kategori);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

        if(Build.VERSION.SDK_INT>9) {
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KategoriItem item = (KategoriItem) parent.getItemAtPosition(position);

                Toast.makeText(KategoriActivity.this, item.getNama_kategori(), Toast.LENGTH_SHORT).show();
            }
        });
        loadJSON();

    }

    private void loadJSON() {
        if(InternetConnection.checkConnection(getApplicationContext())){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Konfigurasi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            KategoriAPI kategoriApi = retrofit.create(KategoriAPI.class);
            Call<JSONKategori> call = kategoriApi.getKategori();

            call.enqueue(new Callback<JSONKategori>() {
                @Override
                public void onResponse(Call<JSONKategori> call, Response<JSONKategori> response) {
                    Log.e("Response: ", response.body().toString());
                    if(response.isSuccessful())
                    {
                        JSONKategori jsonKategori= response.body();
                        mGridData = new ArrayList<>(Arrays.asList(jsonKategori.getPosts()));
                        adapter= new KategoriAdapter(KategoriActivity.this, R.layout.grid_item_kategori, mGridData);
                        mGridView.setAdapter(adapter);

                        adapter.setGridData(mGridData);
                    }else
                    {
                        Toast.makeText(KategoriActivity.this, "GAGAL WE", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONKategori> call, Throwable t) {

                }
            });


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }
}
