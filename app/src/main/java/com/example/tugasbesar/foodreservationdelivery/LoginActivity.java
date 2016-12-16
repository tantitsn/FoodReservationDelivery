package com.example.tugasbesar.foodreservationdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;

/**
 * Created by tanti on 12/12/16.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edtUsername) EditText _Username;
    @BindView(R.id.edtPassword) EditText _Password;
    @BindView(R.id.edtNoMeja) EditText _NoMeja;
    @BindView(R.id.btnLogin) Button _btnLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

       _btnLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(_Username.getText().toString().equals("admin") &&
                       _Password.getText().toString().equals("admin"))
               {
                   Intent i = new Intent(getApplicationContext(),KategoriActivity.class);
                   startActivity(i);
                   finish();

               }else
               {
                   Toast.makeText(LoginActivity.this, "Username dan Password salah", Toast.LENGTH_SHORT).show();

               }
           }
       });



    }
}
