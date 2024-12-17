package com.tvip.surveylance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;

public class splash extends AppCompatActivity {
    String newVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new FetchAppVersionFromGooglePlayStore().execute();

        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent home = new Intent(splash.this, login.class);
                    startActivity(home);
                    finish();
                }
            }, 5000);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);
            alertDialogBuilder.setTitle("Maaf, anda belum terhubung ke internet");
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }
    }
    class FetchAppVersionFromGooglePlayStore extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                return
                        Jsoup.connect("http://play.google.com/store/apps/details?id=com.tvip.surveylance")
                                .timeout(10000)
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .referrer("http://www.google.com")
                                .get()
                                .select("div:containsOwn(Current Version)")
                                .first()
                                .ownText();

            } catch (Exception e) {
                return "";
            }
        }

        protected void onPostExecute(String string) {
            newVersion = string;
            Toast.makeText(getApplicationContext(), newVersion, Toast.LENGTH_SHORT).show();

        }
    }
}