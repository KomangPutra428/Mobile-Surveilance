package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class update_complete extends AppCompatActivity {
    Button selesai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_complete);
        selesai = findViewById(R.id.selesai);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inetnt = new Intent(getBaseContext(), MainActivity.class);
                inetnt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(inetnt);
            }
        });
    }
}