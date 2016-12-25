package com.example.tugasbesar.foodreservationdelivery;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tugasbesar.foodreservationdelivery.configs.InternetConnection;
import com.example.tugasbesar.foodreservationdelivery.configs.Konfigurasi;
import com.example.tugasbesar.foodreservationdelivery.configs.SessionManagement;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tanti on 12/12/16.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edtUsername) EditText _Username;
    @BindView(R.id.edtPassword) EditText _Password;
    @BindView(R.id.edtNoMeja) Spinner _NoMeja;
    @BindView(R.id.btnLogin) Button _btnLogin;

    SessionManagement session;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        session = new SessionManagement(getApplicationContext());
        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_Username.getText().toString().equals("") ||
                        _Password.getText().toString().equals("") ||
                        _NoMeja.getSelectedItem().toString().equals("-Pilih Meja-")){
                    Toast.makeText(LoginActivity.this, "Silahkan Lengkapi Data!", Toast.LENGTH_SHORT).show();
                }else {
                    if (_Username.getText().toString().equals("admin") &&
                            _Password.getText().toString().equals("admin")) {

                        String meja = _NoMeja.getSelectedItem().toString();
                        Intent i = new Intent(getApplicationContext(), KategoriActivity.class);
                        startActivity(i);

                        if(meja.equals("Waitress")){
                            String token = FirebaseInstanceId.getInstance().getToken();
                            registerToken(token);
                        }
                        finish();

                        session.createLoginSession("admin", meja);
                    } else {
                        Toast.makeText(LoginActivity.this, "Username dan Password salah", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        initMeja();
    }

    private void registerToken(String token) {
        RequestBody body = new FormBody.Builder()
                .add("token", token)
                .build();

        Request request = new Request.Builder()
                .url(Konfigurasi.TOKEN_REGISTER_URL)
                .post(body)
                .build();

        try {
            //client.newCall(request).execute();
            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMeja() {
        if(InternetConnection.checkConnection(getApplicationContext())){
            ArrayList<String> groupData;
            groupData = new ArrayList<>();
            groupData.add("-Pilih Meja-");
            groupData.add("Waitress");

            try {
                Request request = new Request.Builder()
                        .url(Konfigurasi.GET_MEJA)
                        .build();

                Response response = client.newCall(request).execute();
                String respon = response.body().string();
                //Log.e("Meja: ", respon);

                JSONObject jsonObject = new JSONObject(respon);
                int sukses = jsonObject.getInt(Konfigurasi.TAG_SUKSES);
                if(sukses == 1){
                    JSONArray jsonArray = jsonObject.getJSONArray("meja");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        groupData.add(obj.getString("id_meja"));
                    }
                }else{
                    groupData.add("Tidak Ada Data!");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, groupData);
            _NoMeja.setAdapter(groupAdapter);
            _NoMeja.setSelection(0);

        }else{
            Toast.makeText(this, "Oops! Mohon periksa jaringan internet anda.", Toast.LENGTH_SHORT).show();
        }
    }
}
