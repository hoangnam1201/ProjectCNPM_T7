package com.example.busstation;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView signup, signup2;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup=(TextView)findViewById(R.id.tv_signup);
        signup2=(TextView)findViewById(R.id.tv_signup2);
        btnLogin=(Button)findViewById(R.id.btn_Login);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openSignUp();
            }
        });
        signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();
            }
        });
    }
    public void openSignUp(){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
    public void openHome(){
        Intent intent = new Intent(this,HomeNavigation.class);
        startActivity(intent);
    }
}