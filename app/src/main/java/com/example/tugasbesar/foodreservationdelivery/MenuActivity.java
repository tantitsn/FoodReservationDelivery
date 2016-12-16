package com.example.tugasbesar.foodreservationdelivery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
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
    @BindView(R.id.id_produk) TextView id_produk;
    private ProgressDialog pDialog;
    private MenuItemAdapter adapter;
    private ArrayList<MenuItem> mGridData;
    private Spinner spQty;
    private Button btBeli, btBatal;


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

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = (MenuItem) parent.getItemAtPosition(position);
                id_produk.setText(item.getId_produk());
                showDialogs();

            }
        });

        loadJSON();
    }

    private void showDialogs() {
        AlertDialog dialog = new DialogQty(MenuActivity.this);
        dialog.show();
    }

    private void loadJSON() {
        if(InternetConnection.checkConnection(getApplicationContext())){

            String id_kategori = getIntent().getStringExtra("id_kategori");
            Map<String, String> datas = new HashMap<>();
            datas.put("id_kategori", id_kategori);

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

    private class DialogQty extends AlertDialog {
        public DialogQty(Context context) {
            super(context);

            setTitle("Jumlah Pemesanan");
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.dialog_beli, null);
            setView(v);

            spQty = (Spinner) v.findViewById(R.id.spQty);
            btBeli = (Button) v.findViewById(R.id.btbeli);
            btBatal = (Button) v.findViewById(R.id.btbatal);

            ArrayList<String> list;
            list = new ArrayList<>();
            list.add("--pilih--");
            for (int i = 1; i <= 10; i++){
                list.add(String.valueOf(i));
            }

            ArrayAdapter<String> qtyAdapter = new ArrayAdapter<>(getApplication(),
                    R.layout.spinner_item, list);
            assert spQty != null;
            spQty.setAdapter(qtyAdapter);
            spQty.setSelection(0);

            btBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }
}
