package com.example.tugasbesar.foodreservationdelivery;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.example.tugasbesar.foodreservationdelivery.adapter.KategoriAdapter;
import com.example.tugasbesar.foodreservationdelivery.configs.InternetConnection;
import com.example.tugasbesar.foodreservationdelivery.models.KategoriItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tanti on 12/12/16.
 */
public class KategoriActivity extends AppCompatActivity {

    @BindView(R.id.gridView) GridView mGridView;
    @BindView(R.id.parent) CoordinatorLayout _parent;
    private ProgressDialog pDialog;
    private KategoriAdapter adapter;
    private ArrayList<KategoriItem> mGridData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_kategori);
        ButterKnife.bind(this);
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
      //  if(InternetConnection.checkConnection(getApplicationContext())){

      //  }
    }
}
