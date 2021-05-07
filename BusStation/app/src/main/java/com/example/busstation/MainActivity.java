package com.example.busstation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.busstation.models.User;
import com.example.busstation.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    TextView signup, signup2, tvError;
    Button btnLogin;
    Retrofit retrofit;
    UserService userService;
    EditText edtUsername, edtPassword;
    ProgressBar progressBar;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        if(sharedpreferences.getString("userAuthId",null) != null){
            Intent intent = new Intent(getApplicationContext(), HomeNavigation.class);
            startActivity(intent);
            return;
        }
        anhXa();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://busapbe.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);
        btnLogin.setOnClickListener(v -> onLogin());
        signup.setOnClickListener(v -> openSignUp());
        signup2.setOnClickListener(v -> openSignUp());

    }
    public void anhXa(){
        signup = findViewById(R.id.tv_signup);
        signup2 = findViewById(R.id.tv_signup2);
        btnLogin = findViewById(R.id.btn_Login);
        edtUsername = findViewById(R.id.edtUsernamelg);
        edtPassword = findViewById(R.id.edtPasswordlg);
        tvError = findViewById(R.id.tvErrorlg);
        progressBar = this.findViewById(R.id.progressBar);
    }

    public void openSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onLogin() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setText("");

        Call<User> call = userService.login(edtUsername.getText().toString(), edtPassword.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() == null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    tvError.setText("invalid username or password");
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), HomeNavigation.class);
                startActivity(intent);

                User user = response.body();
                editor.putString("userAuthId",user.get_id());
                editor.commit();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                tvError.setText("Error! An error occurred. Please try again later");
            }
        });

    }
}