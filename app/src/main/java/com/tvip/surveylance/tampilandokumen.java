package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class tampilandokumen extends AppCompatActivity {
    MaterialToolbar keterangan;
    TextView jenis;
    ImageView gambar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampilandokumen);
        HttpsTrustManager.allowAllSSL();
        keterangan = findViewById(R.id.keterangan);
        jenis = findViewById(R.id.jenis);
        gambar = findViewById(R.id.gambar);
        keterangan.setTitle(getIntent().getStringExtra("Jenis"));
        jenis.setText(getIntent().getStringExtra("Jenis") +" Pelanggan");

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        String url = "https://hrd.tvip.co.id/eis/uploads/dokumen_customer/" + getIntent().getStringExtra("Jenis") + "/" + getIntent().getStringExtra("id") + "_" + szId +".jpeg";
        System.out.println("Hasil URL = " + url);

        Glide.with(tampilandokumen.this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(gambar);
    }
}