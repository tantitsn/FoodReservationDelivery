package com.example.tugasbesar.foodreservationdelivery.configs;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by amarullah87 on 04/07/16.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        registerToken(token);
    }

    private void registerToken(String token) {

        OkHttpClient client = new OkHttpClient();
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
}
