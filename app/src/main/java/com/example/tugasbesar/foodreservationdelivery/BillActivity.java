package com.example.tugasbesar.foodreservationdelivery;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tugasbesar.foodreservationdelivery.adapter.CartRecyclerAdapter;
import com.example.tugasbesar.foodreservationdelivery.api.JSONCart;
import com.example.tugasbesar.foodreservationdelivery.configs.InternetConnection;
import com.example.tugasbesar.foodreservationdelivery.configs.Konfigurasi;
import com.example.tugasbesar.foodreservationdelivery.configs.SessionManagement;
import com.example.tugasbesar.foodreservationdelivery.interfaces.MenuAPI;
import com.example.tugasbesar.foodreservationdelivery.models.CartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    @BindView(R.id.txt_subtotal) TextView _total;
    @BindView(R.id.txt_qty) TextView _qty;

    @BindView(R.id.tutup) Button _tutup;
    @BindView(R.id.pesan) Button _pesan;

    private ArrayList<CartItem> items;
    private SessionManagement session;
    OkHttpClient client = new OkHttpClient();
    okhttp3.Response response;

    private ProgressDialog pDialog;

    public static BillActivity me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.halaman_bill);
        ButterKnife.bind(this);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        session = new SessionManagement(getApplicationContext());
        me = this;

        if(Build.VERSION.SDK_INT>9) {
            StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        loadJSON();

        _tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> user = session.getUserDetails();
                String meja = user.get(SessionManagement.KEY_MEJA);

                new SavePesanan().execute(meja);
            }
        });
    }

    private void loadJSON() {
        if(InternetConnection.checkConnection(getApplicationContext()))
        {
            HashMap<String,String> user = session.getUserDetails();
            final String meja = user.get(SessionManagement.KEY_MEJA);
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

                        new GetTotalBelanja().execute(meja);
                    }else{
                        Toast.makeText(BillActivity.this, "Oops! Terjadi Kesalahan.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JSONCart> call, Throwable t) {
                    Toast.makeText(BillActivity.this, "Oops! Terjadi Kesalahan.", Toast.LENGTH_SHORT).show();
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

    private class GetTotalBelanja extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(final String... params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Request request = new Request.Builder()
                                .url(Konfigurasi.GET_TOTAL_CART + "?id_meja="+params[0])
                                .build();
                        response = client.newCall(request).execute();
                        String respon = response.body().string();

                        JSONObject jsonObject = new JSONObject(respon);
                        JSONArray jsonArray = jsonObject.getJSONArray("total");
                        JSONObject object = jsonArray.getJSONObject(0);

                        _qty.setText(object.getString("tqty"));
                        _total.setText("Rp. " + object.getString("ttot"));

                        if(object.getString("status").equals("1") || object.getString("status_pesan").equals("0")){
                            _pesan.setVisibility(View.INVISIBLE);
                            _pesan.setClickable(false);
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }
    }

    private class SavePesanan extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BillActivity.this);
            pDialog.setMessage("Menyimpan Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            if(!(BillActivity.this.isFinishing())){
                pDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody requestBody = new FormBody.Builder()
                        .add("id_meja", params[0])
                        .build();

                Request request = new Request.Builder()
                        .url(Konfigurasi.SAVE_PESANAN)
                        .post(requestBody)
                        .build();

                response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(s);
                int sukses = jsonObject.getInt(Konfigurasi.TAG_SUKSES);

                if(sukses == 1){
                    Toast.makeText(BillActivity.this, "Pesanan Berhasil Disimpan.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(BillActivity.this, "Oops! Terjadi Kesalahan.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
