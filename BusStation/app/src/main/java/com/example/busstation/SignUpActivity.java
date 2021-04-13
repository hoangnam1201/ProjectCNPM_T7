package com.example.busstation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login= (TextView)findViewById(R.id.tv_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });
    }
    public void openLogin(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}