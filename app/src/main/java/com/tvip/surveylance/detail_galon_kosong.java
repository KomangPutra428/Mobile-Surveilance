package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class detail_galon_kosong extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    TextView kodepelanggan;
    TextView bulantahun;

    TextView namapelanggan, segment, depo;
    TextView jumlahgalon, rata_rata, standard;

    EditText editstokgalon, editluasgudang;
    Button batal, proses;

    TextView tanggal;

    SweetAlertDialog pDialog;

    MaterialCardView linearbutton;
    LinearLayout linearstock;

    TextView stok_galon_kosong, luas_gudang;
    Chip total_galon, hasil;

    MaterialCardView detail_perbandingan;
    MaterialButton edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_galon_kosong);

        kodepelanggan = findViewById(R.id.kodepelanggan);
        bulantahun = findViewById(R.id.bulantahun);
        linearbutton = findViewById(R.id.linearbutton);

        editstokgalon = findViewById(R.id.editstokgalon);
        editluasgudang = findViewById(R.id.editluasgudang);

        batal = findViewById(R.id.batal);
        proses = findViewById(R.id.proses);
        edit = findViewById(R.id.edit);

        namapelanggan = findViewById(R.id.namapelanggan);
        segment = findViewById(R.id.segment);
        depo = findViewById(R.id.depo);

        jumlahgalon = findViewById(R.id.jumlahgalon);
        rata_rata = findViewById(R.id.rata_rata);
        standard = findViewById(R.id.standard);

        linearstock = findViewById(R.id.linearstock);

        stok_galon_kosong = findViewById(R.id.stok_galon_kosong);
        luas_gudang = findViewById(R.id.luas_gudang);

        total_galon = findViewById(R.id.total_galon);
        hasil = findViewById(R.id.hasil);

        tanggal = findViewById(R.id.tanggal);

        detail_perbandingan = findViewById(R.id.detail_perbandingan);

        editstokgalon.addTextChangedListener(new MyNumberWatcher_3Digit(editstokgalon));
        editluasgudang.addTextChangedListener(new MyNumberWatcher_3Digit(editluasgudang));


        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        String szName = sharedPreferences.getString("szName", null);

        kodepelanggan.setText(szId);
        namapelanggan.setText(szName);
        segment.setText(getIntent().getStringExtra("segment"));
        depo.setText(getIntent().getStringExtra("depo"));

        bulantahun.setText(getIntent().getStringExtra("bulantahun"));

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String total2 = formatter.format(Integer.parseInt(getIntent().getStringExtra("total")));
        String rata = formatter.format(Integer.parseInt(getIntent().getStringExtra("standar")));
        String standar = formatter.format(Integer.parseInt(getIntent().getStringExtra("standar")));

        String totaltitik = total2.replace(",", ".");
        String ratatitik = rata.replace(",", ".");
        String standartitik = standar.replace(",", ".");


        jumlahgalon.setText(totaltitik  + " Galon");
        rata_rata.setText(ratatitik + " Galon");
        standard.setText(standartitik + " Galon");
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        String currentDateandTime = sdf.format(new Date());

        if(!getIntent().getStringExtra("bulantahun").equals(currentDateandTime)){
            edit.setVisibility(View.GONE);
        }

        if(getIntent().getStringExtra("tglupdate") == null){
            tanggal.setText("Belum Ada Survey");
        } else {
            tanggal.setText(convertFormat(getIntent().getStringExtra("tglupdate")));
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearbutton.setVisibility(View.VISIBLE);
                linearstock.setVisibility(View.VISIBLE);
                detail_perbandingan.setVisibility(View.GONE);
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(!getIntent().getStringExtra("luas").equals("null")){
            linearbutton.setVisibility(View.GONE);
            linearstock.setVisibility(View.GONE);
            detail_perbandingan.setVisibility(View.VISIBLE);

            String stock_kosong = formatter.format(Integer.parseInt(getIntent().getStringExtra("stock_kosong")));
            String luas = formatter.format(Integer.parseInt(getIntent().getStringExtra("luas")));
            String totalgalon = formatter.format(Integer.parseInt(getIntent().getStringExtra("luas")) * 48);

            String stock_kosongtitik = stock_kosong.replace(",", ".");
            String luastitik = luas.replace(",", ".");
            String totaltitik2 = totalgalon.replace(",", ".");

            editstokgalon.setText(getIntent().getStringExtra("stock_kosong"));
            editluasgudang.setText(getIntent().getStringExtra("luas"));

            stok_galon_kosong.setText(stock_kosongtitik + " Galon");
            luas_gudang.setText(luastitik + " m2");
            total_galon.setText(totaltitik2 + " Galon");

            int total = Integer.parseInt(getIntent().getStringExtra("luas")) * 48;

            if(total >= Integer.parseInt(getIntent().getStringExtra("stock_kosong"))){

                int colorInt = getResources().getColor(R.color.background_green);
                ColorStateList csl = ColorStateList.valueOf(colorInt);

                int colorInt2 = getResources().getColor(R.color.border_green);
                ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

                hasil.setText("SPACE IDEAL");

                hasil.setChipBackgroundColor(csl);
                hasil.setChipStrokeColor(csl2);
                hasil.setTextColor(Color.parseColor("#2ECC71"));

                total_galon.setChipBackgroundColor(csl);
                total_galon.setChipStrokeColor(csl2);
                total_galon.setTextColor(Color.parseColor("#2ECC71"));

                detail_perbandingan.setCardBackgroundColor(Color.parseColor("#E8FFF2"));
                detail_perbandingan.setStrokeColor(Color.parseColor("#B9EED0"));
            } else if(total < Integer.parseInt(getIntent().getStringExtra("stock_kosong"))){
                int colorInt = getResources().getColor(R.color.background_red);
                ColorStateList csl = ColorStateList.valueOf(colorInt);

                int colorInt2 = getResources().getColor(R.color.border_red);
                ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

                hasil.setText("SPACE TIDAK IDEAL");
                hasil.setChipBackgroundColor(csl);
                hasil.setChipStrokeColor(csl2);
                hasil.setTextColor(Color.parseColor("#FB4141"));

                total_galon.setChipBackgroundColor(csl);
                total_galon.setChipStrokeColor(csl2);
                total_galon.setTextColor(Color.parseColor("#FB4141"));

                detail_perbandingan.setCardBackgroundColor(Color.parseColor("#FFF1F1"));
                detail_perbandingan.setStrokeColor(Color.parseColor("#FEC0C0"));
            }


        } else {
            linearbutton.setVisibility(View.VISIBLE);
            linearstock.setVisibility(View.VISIBLE);
            detail_perbandingan.setVisibility(View.GONE);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            Date date = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("M-yyyy");
            String dateOutput = format.format(date);

            String[] parts = dateOutput.split("-");
            String bulan = parts[0];
            String tahun = parts[1];

            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/ratio_galon_kosong/ratio/index_LastDate?id="+szId+"&bulan="+bulan+"&tahun="+tahun,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                final JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    if(!movieObject.getString("luas_gudang").equals("null")){
                                        editluasgudang.setText(movieObject.getString("luas_gudang"));
                                        editluasgudang.setFocusable(false);
                                        editluasgudang.setLongClickable(false);
                                    }



                                }


                            } catch(JSONException e){
                                e.printStackTrace();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", "admin", "Databa53");
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
            RequestQueue requestQueue = Volley.newRequestQueue(detail_galon_kosong.this);
            requestQueue.add(stringRequest);
        }

        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editstokgalon.getText().toString().length() == 0){
                    editstokgalon.setError("Isi Stock Galon");
                } else if(editluasgudang.getText().toString().length() == 0){
                    editluasgudang.setError("Isi Luas Gudang");
                } else {
                    pDialog = new SweetAlertDialog(detail_galon_kosong.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setCancelable(false);
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.show();

                    StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://assessment.tvip.co.id/rest_server/ratio_galon_kosong/ratio/index",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    pDialog.dismissWithAnimation();
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                                        String currentDateandTime = sdf.format(new Date());

                                        Intent succes = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                                        startActivity(succes);
                                        linearbutton.setVisibility(View.GONE);
                                        linearstock.setVisibility(View.GONE);
                                        detail_perbandingan.setVisibility(View.VISIBLE);

                                        tanggal.setText(currentDateandTime);

                                        edit.setVisibility(View.VISIBLE);

                                        String stock_kosong = formatter.format(Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editstokgalon.getText().toString())));
                                        String luas = formatter.format(Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editluasgudang.getText().toString())));
                                        String totalgalon = formatter.format(Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editluasgudang.getText().toString())) * 48);


                                        stok_galon_kosong.setText(stock_kosong + " Galon");
                                        luas_gudang.setText(luas + " m2");
                                        total_galon.setText(totalgalon + " Galon");

                                        int total = Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editluasgudang.getText().toString())) * 48;
                                        if(total >= Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editstokgalon.getText().toString()))){

                                            int colorInt = getResources().getColor(R.color.background_green);
                                            ColorStateList csl = ColorStateList.valueOf(colorInt);

                                            int colorInt2 = getResources().getColor(R.color.border_green);
                                            ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

                                            hasil.setText("SPACE IDEAL");

                                            hasil.setChipBackgroundColor(csl);
                                            hasil.setChipStrokeColor(csl2);
                                            hasil.setTextColor(Color.parseColor("#2ECC71"));

                                            total_galon.setChipBackgroundColor(csl);
                                            total_galon.setChipStrokeColor(csl2);
                                            total_galon.setTextColor(Color.parseColor("#2ECC71"));

                                            detail_perbandingan.setCardBackgroundColor(Color.parseColor("#E8FFF2"));
                                            detail_perbandingan.setStrokeColor(Color.parseColor("#B9EED0"));
                                        } else if(total < Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editstokgalon.getText().toString()))){
                                            int colorInt = getResources().getColor(R.color.background_red);
                                            ColorStateList csl = ColorStateList.valueOf(colorInt);

                                            int colorInt2 = getResources().getColor(R.color.border_red);
                                            ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

                                            hasil.setText("SPACE TIDAK IDEAL");
                                            hasil.setChipBackgroundColor(csl);
                                            hasil.setChipStrokeColor(csl2);
                                            hasil.setTextColor(Color.parseColor("#FB4141"));

                                            total_galon.setChipBackgroundColor(csl);
                                            total_galon.setChipStrokeColor(csl2);
                                            total_galon.setTextColor(Color.parseColor("#FB4141"));

                                            detail_perbandingan.setCardBackgroundColor(Color.parseColor("#FFF1F1"));
                                            detail_perbandingan.setStrokeColor(Color.parseColor("#FEC0C0"));
                                        }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismissWithAnimation();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String currentDateandTime = sdf.format(new Date());

                            Intent succes = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                            startActivity(succes);
                            linearbutton.setVisibility(View.GONE);
                            linearstock.setVisibility(View.GONE);
                            detail_perbandingan.setVisibility(View.VISIBLE);

                            tanggal.setText(currentDateandTime);

                            edit.setVisibility(View.VISIBLE);

                            String stock_kosong = formatter.format(Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editstokgalon.getText().toString())));
                            String luas = formatter.format(Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editluasgudang.getText().toString())));
                            String totalgalon = formatter.format(Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editluasgudang.getText().toString())) * 48);


                            stok_galon_kosong.setText(stock_kosong + " Galon");
                            luas_gudang.setText(luas + " m2");
                            total_galon.setText(totalgalon + " Galon");

                            int total = Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editluasgudang.getText().toString())) * 48;
                            if(total >= Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editstokgalon.getText().toString()))){

                                int colorInt = getResources().getColor(R.color.background_green);
                                ColorStateList csl = ColorStateList.valueOf(colorInt);

                                int colorInt2 = getResources().getColor(R.color.border_green);
                                ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

                                hasil.setText("SPACE IDEAL");

                                hasil.setChipBackgroundColor(csl);
                                hasil.setChipStrokeColor(csl2);
                                hasil.setTextColor(Color.parseColor("#2ECC71"));

                                total_galon.setChipBackgroundColor(csl);
                                total_galon.setChipStrokeColor(csl2);
                                total_galon.setTextColor(Color.parseColor("#2ECC71"));

                                detail_perbandingan.setCardBackgroundColor(Color.parseColor("#E8FFF2"));
                                detail_perbandingan.setStrokeColor(Color.parseColor("#B9EED0"));
                            } else if(total < Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(editstokgalon.getText().toString()))){
                                int colorInt = getResources().getColor(R.color.background_red);
                                ColorStateList csl = ColorStateList.valueOf(colorInt);

                                int colorInt2 = getResources().getColor(R.color.border_red);
                                ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

                                hasil.setText("SPACE TIDAK IDEAL");
                                hasil.setChipBackgroundColor(csl);
                                hasil.setChipStrokeColor(csl2);
                                hasil.setTextColor(Color.parseColor("#FB4141"));

                                total_galon.setChipBackgroundColor(csl);
                                total_galon.setChipStrokeColor(csl2);
                                total_galon.setTextColor(Color.parseColor("#FB4141"));

                                detail_perbandingan.setCardBackgroundColor(Color.parseColor("#FFF1F1"));
                                detail_perbandingan.setStrokeColor(Color.parseColor("#FEC0C0"));
                            }

                        }

                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<String, String>();
                            String creds = String.format("%s:%s", "admin", "Databa53");
                            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                            params.put("Authorization", auth);
                            return params;
                        }


                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String currentDateandTime = sdf.format(new Date());

                            params.put("id_ratio", getIntent().getStringExtra("ID"));
                            params.put("nik_survey", sharedPreferences.getString("nik_karyawan", null));

                            if(getIntent().getStringExtra("dtmSurvey").equals("null")){
                                params.put("dtmSurvey", currentDateandTime);
                            } else {
                                params.put("dtmSurvey", getIntent().getStringExtra("dtmSurvey"));
                            }



                            params.put("dtmLastUpdate", currentDateandTime);

                            params.put("jumlah_stok", MyNumberWatcher_3Digit.trimCommaOfString(editstokgalon.getText().toString()));
                            params.put("luas_gudang", MyNumberWatcher_3Digit.trimCommaOfString(editluasgudang.getText().toString()));



                            return params;
                        }

                    };
                    stringRequest2.setRetryPolicy(
                            new DefaultRetryPolicy(
                                    500000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                            )
                    );
                    RequestQueue requestQueue2 = Volley.newRequestQueue(detail_galon_kosong.this);
                    requestQueue2.add(stringRequest2);
                }
            }
        });



    }
    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return convetDateFormat.format(date);
    }
}