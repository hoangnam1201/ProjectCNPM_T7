package com.example.busstation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busstation.models.User;
import com.example.busstation.services.UserService;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    Retrofit retrofit;
    UserService userService;
    TextView login,tvError;

    EditText edtFullName, edtUsername, edtPassword, edtEmail, edtRepeatPassword;
    Button btnApply;
    Boolean isExistEmail, isExistUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        AnhXa();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://busapbe.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);


        login = (TextView) findViewById(R.id.tv_login);

        login.setOnClickListener(v -> openLogin());

        btnApply.setOnClickListener(v -> {
            if (!Pattern.matches("[a-zA-z\\s]{3,}",edtFullName.getText().toString().trim())){
                tvError.setText("invalid full name");
                return;
            }
            if (!Pattern.matches("^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$",edtEmail.getText().toString().trim())){
                tvError.setText("invalid email");
                return;
            }
            if (!Pattern.matches("[a-zA-z0-9\\_]{5,}",edtUsername.getText().toString().trim())){
                tvError.setText("invalid username");
                return;
            }
            if(edtPassword.getText().toString().length() < 8){
                tvError.setText("The minimum length of the password is 8 characters ");
                return;
            }
            if(!edtRepeatPassword.getText().toString().trim().equals(edtPassword.getText().toString())){
                tvError.setText("repeat passwords that are not the same as passwords ");
                return;
            }
//            try {
//                tvError.setText(checkExistEmail()+"");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if(isExistEmail == true){
//                tvError.setText("email already exist");
//                return;
//            }
//            if(isExistUsername){
//                tvError.setText("username already exist");
//                return;
//            }
            createUser();
        });

    }

    public void checkExistUsername(){
        Call<Boolean> call = userService.checkExistUsername(edtUsername.getText().toString().trim());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

//                isExistUsername = response.body();
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public Boolean checkExistEmail() throws IOException {
        Call<Boolean> call = userService.checkExistEmail(edtEmail.getText().toString().trim());
        Response<Boolean> response = call.execute();
        Boolean isExit = response.body();
        return isExit;
    }

    public void createUser() {
        Call<List<User>> call = userService.createUser(
                edtFullName.getText().toString().trim(),
                edtUsername.getText().toString().trim(),
                edtEmail.getText().toString().trim(),
                edtPassword.getText().toString().trim()
        );
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error! An error occurred. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void AnhXa() {
        tvError = findViewById(R.id.tvError);
        btnApply = findViewById(R.id.btn_Apply);
        edtFullName = findViewById(R.id.edtFullName);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRepeatPassword = findViewById(R.id.edtRepeatPassword);
    }

    public void openLogin() {

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }
}