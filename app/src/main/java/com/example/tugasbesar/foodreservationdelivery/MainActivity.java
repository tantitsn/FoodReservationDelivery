package com.example.tugasbesar.foodreservationdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.tugasbesar.foodreservationdelivery.configs.SessionManagement;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imgMenu) ImageView _menu;
    @BindView(R.id.imgPemesanan) ImageView _pemesanan;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FirebaseMessaging.getInstance().subscribeToTopic("FDR");
        FirebaseInstanceId.getInstance().getToken();

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        _menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), KategoriActivity.class));
            }
        });

        _pemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BillActivity.class));
            }
        });
    }
}
