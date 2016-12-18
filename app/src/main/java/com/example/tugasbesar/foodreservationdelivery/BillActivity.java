package com.example.tugasbesar.foodreservationdelivery;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.tugasbesar.foodreservationdelivery.adapter.CartRecyclerAdapter;
import com.example.tugasbesar.foodreservationdelivery.api.JSONCart;
import com.example.tugasbesar.foodreservationdelivery.configs.InternetConnection;
import com.example.tugasbesar.foodreservationdelivery.configs.Konfigurasi;
import com.example.tugasbesar.foodreservationdelivery.configs.SessionManagement;
import com.example.tugasbesar.foodreservationdelivery.interfaces.MenuAPI;
import com.example.tugasbesar.foodreservationdelivery.models.CartItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amarullah87 on 18/12/16.
 */
public class BillActivity extends AppCompatActivity{
    @BindView(R.id.rvOrder)RecyclerView _rvOrder;

    private ArrayList<CartItem> items;
    private SessionManagement session;

    public static BillActivity me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halaman_bill);
        ButterKnife.bind(this);
        session = new SessionManagement(getApplicationContext());
        me = this;

        if(Build.VERSION.SDK_INT>9) {
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadJSON();

    }

    private void loadJSON() {
        if(InternetConnection.checkConnection(getApplicationContext()))
        {
            HashMap<String,String> user = session.getUserDetails();
            String meja = user.get(SessionManagement.KEY_MEJA);
            Map<String, String> datas = new HashMap<>();
            datas.put("id_meja", meja);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Konfigurasi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MenuAPI request = retrofit.create(MenuAPI.class);
            Call<JSONCart> call = request.getCart(datas);

            call.enqueue(new Callback<JSONCart>() {
                @Override
                public void onResponse(Call<JSONCart> call, Response<JSONCart> response) {
                    if (response.isSuccessful()){
                        JSONCart jsonCart = response.body();
                        items = new ArrayList<>(Arrays.asList(jsonCart.getPosts()));
                        _rvOrder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        CartRecyclerAdapter adapter = new CartRecyclerAdapter(getApplicationContext(),items);
                        _rvOrder.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<JSONCart> call, Throwable t) {

                }
            });

        }

    }

    public void refreshItem(){
        if(items != null){
            items.clear();
        }
        loadJSON();
    }
}
