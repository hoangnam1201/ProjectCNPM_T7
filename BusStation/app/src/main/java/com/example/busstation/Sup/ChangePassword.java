package com.example.busstation.Sup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busstation.AccountSetting;
import com.example.busstation.HomeNavigation;
import com.example.busstation.R;
import com.example.busstation.SearchBus;

public class ChangePassword extends AppCompatActivity {
    Button btnSave, btnok;
    ProgressDialog progressDialog;
    View v;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        
        //Anhxa
        btnSave = this.findViewById(R.id.btnSaveNewPass);


        //Truy cap phan tu xml khac
        LayoutInflater inflater = getLayoutInflater();
        View myView = inflater.inflate(R.layout.progress_dialog, null);
//        btnok = (Button) myView.findViewById(R.id.btnOK);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ChangePassword.this);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
                finish();

                Toast toast = Toast.makeText(ChangePassword.this, "Saved", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

//        btnok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

    }
    public void onBackPressed() {
        super.onBackPressed();
        progressDialog.dismiss();
        finish();
    }
}