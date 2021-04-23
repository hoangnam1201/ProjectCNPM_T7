package com.example.busstation;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.busstation.models.User;
import com.example.busstation.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView signup, signup2, tvError;
    Button btnLogin;
    Retrofit retrofit;
    UserService userService;
    EditText edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        retrofit = new Retrofit.Builder()
                .baseUrl("https://busapbe.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);

        signup = (TextView) findViewById(R.id.tv_signup);
        signup2 = (TextView) findViewById(R.id.tv_signup2);
        btnLogin = (Button) findViewById(R.id.btn_Login);
        edtUsername = (EditText) findViewById(R.id.edtUsernamelg);
        edtPassword = (EditText) findViewById(R.id.edtPasswordlg);
        tvError = (TextView) findViewById(R.id.tvErrorlg);


        btnLogin.setOnClickListener(v -> openHome());

        signup.setOnClickListener(v -> openSignUp());
        signup2.setOnClickListener(v -> openSignUp());
    }

    public void openSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void openHome() {
        Call<User> call = userService.getUser(edtUsername.getText().toString(), edtPassword.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() == null) {
                    tvError.setText("invalid username or password");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), HomeNavigation.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                tvError.setText("Error! An error occurred. Please try again later");
            }
        });

    }
}