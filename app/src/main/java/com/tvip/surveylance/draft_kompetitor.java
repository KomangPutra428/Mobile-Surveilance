package com.tvip.surveylance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.card.MaterialCardView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.tvip.surveylance.pojo.draftkompetitor_pojo;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class draft_kompetitor extends AppCompatActivity {
    TextView add;
    ArrayList<String> idproducts = new ArrayList<>();
    ArrayList<String> products = new ArrayList<>();
    String id_products;

    ArrayList<String> idvolume = new ArrayList<>();
    ArrayList<String> volume = new ArrayList<>();
    String id_volume;

    ArrayList<String> iduom = new ArrayList<>();
    ArrayList<String> uom = new ArrayList<>();
    String id_uom;

    ArrayList<String> idkiriman = new ArrayList<>();
    ArrayList<String> kiriman = new ArrayList<>();
    String id_kiriman;

    SweetAlertDialog pDialog;
    SharedPreferences sharedPreferences;
    String mDateStart, mDateEnd;
    List<draftkompetitor_pojo> draftkompetitorPojoList = new ArrayList<>();
    ListViewAdapterDraftKompetitor adapterDraftKompetitor;

    Button batal, simpan;
    ListView draft;
    RelativeLayout peringatan;

    MaterialCardView list_kompetitor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_kompetitor);
        HttpsTrustManager.allowAllSSL();
        add = findViewById(R.id.add);
        list_kompetitor = findViewById(R.id.list_kompetitor);

        batal = findViewById(R.id.batal);
        simpan = findViewById(R.id.simpan);

        draft = findViewById(R.id.draft);
        peringatan = findViewById(R.id.peringatan);

        list_kompetitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), list_survey_kompetitor.class);
                startActivity(intent);
            }
        });


        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(draftkompetitorPojoList.size() == 0){
                    new SweetAlertDialog(draft_kompetitor.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Data Masih Kosong")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    getDataSiapSimpan();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                products.clear();
                volume.clear();
                uom.clear();
                kiriman.clear();

                idproducts.clear();
                idvolume.clear();
                iduom.clear();
                idkiriman.clear();

                final Dialog dialog = new Dialog(draft_kompetitor.this);
                dialog.setContentView(R.layout.add_kompetitor);

                TextInputLayout textproduklain = dialog.findViewById(R.id.textproduklain);

                TextInputLayout hargabelilayout = dialog.findViewById(R.id.hargabelilayout);
                TextInputLayout hargajuallayout = dialog.findViewById(R.id.hargajuallayout);

                AutoCompleteTextView nama_produk = dialog.findViewById(R.id.nama_produk);
                AutoCompleteTextView ukuran_produk = dialog.findViewById(R.id.ukuran_produk);
                AutoCompleteTextView isi = dialog.findViewById(R.id.isi);
                AutoCompleteTextView pilihpengirim = dialog.findViewById(R.id.pilihpengirim);

                EditText hargabeli = dialog.findViewById(R.id.hargabeli);
                EditText hargajual = dialog.findViewById(R.id.hargajual);
                EditText produklain = dialog.findViewById(R.id.produklain);
                EditText namapengirim = dialog.findViewById(R.id.namapengirim);

                EditText bulanlalu = dialog.findViewById(R.id.bulanlalu);
                EditText bulanini = dialog.findViewById(R.id.bulanini);
                EditText estimasimargin = dialog.findViewById(R.id.estimasimargin);

                EditText namaprogram = dialog.findViewById(R.id.namaprogram);
                EditText periodeprogram = dialog.findViewById(R.id.periodeprogram);
                EditText mekanismeprogram = dialog.findViewById(R.id.mekanismeprogram);
                EditText targetprogram = dialog.findViewById(R.id.targetprogram);
                EditText keterangan = dialog.findViewById(R.id.keterangan);

                Button batal = dialog.findViewById(R.id.batal);
                Button tambahkan = dialog.findViewById(R.id.tambahkan);

                hargabeli.addTextChangedListener(new MyNumberWatcher_3Digit(hargabeli));
                hargajual.addTextChangedListener(new MyNumberWatcher_3Digit(hargajual));
                estimasimargin.addTextChangedListener(new MyNumberWatcher_3Digit(estimasimargin));
                bulanlalu.addTextChangedListener(new MyNumberWatcher_3Digit(bulanlalu));
                bulanini.addTextChangedListener(new MyNumberWatcher_3Digit(bulanini));
                targetprogram.addTextChangedListener(new MyNumberWatcher_3Digit(targetprogram));

                nama_produk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        id_products = idproducts.get(position).toString();
                    }
                });

                ukuran_produk.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if(ukuran_produk.getText().toString().contains("Gallon")){
                            hargabelilayout.setHint("Harga Beli/Gallon");
                            hargajuallayout.setHint("Harga Jual/Gallon");
                        } else {
                            hargabelilayout.setHint("Harga Beli/Box");
                            hargajuallayout.setHint("Harga Jual/Box");
                        }
                    }
                });


                ukuran_produk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        id_uom = iduom.get(position).toString();
                        if(ukuran_produk.getText().toString().contains("Gallon")){
                            id_volume = idvolume.get(0).toString();
                            isi.setText(volume.get(0).toString());
                            isi.setDropDownHeight(0);
                        } else {
                            isi.setText("");
                            isi.setDropDownHeight(750);
                        }
                    }
                });

                nama_produk.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(nama_produk.getText().toString().contains("LAIN")){
                            textproduklain.setVisibility(View.VISIBLE);
                        } else {
                            textproduklain.setVisibility(View.GONE);
                        }
                    }
                });



                isi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        id_volume = idvolume.get(position).toString();

                    }
                });

                pilihpengirim.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        id_kiriman = idkiriman.get(position).toString();

                    }
                });


                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tambahkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(nama_produk.getText().toString().length() == 0){
                            nama_produk.setError("Isi Produk");
                        } else if(nama_produk.getText().toString().contains("LAIN")){
                            if (produklain.getText().toString().length() ==0){
                                produklain.setError("Isi Produk Lain");
                            } else if (ukuran_produk.getText().toString().length() ==0){
                                ukuran_produk.setError("Isi Ukuran Produk");
                            } else if (isi.getText().toString().length() ==0){
                                isi.setError("Isi Jumlah Per Box");
                            } else if (hargabeli.getText().toString().length() ==0){
                                hargabeli.setError("Isi Harga Beli");
                            } else if (hargajual.getText().toString().length() ==0){
                                hargajual.setError("Isi Harga Jual");
                            } else if (Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(estimasimargin.getText().toString())) < 0){
                                estimasimargin.setError("Harga Jual Harus Lebih Besar Daripada Harga Beli");
                            } else if (pilihpengirim.getText().toString().length() ==0){
                                pilihpengirim.setError("Pilih Pengirim");
                            } else if (namapengirim.getText().toString().length() ==0){
                                namapengirim.setError("Isi Nama Pengirim");
                            } else if (bulanlalu.getText().toString().length() ==0){
                                bulanlalu.setError("Isi Rata-rata Penjualan Pada Bulan Lalu");
                            } else if (bulanini.getText().toString().length() ==0){
                                bulanini.setError("Isi Rata-rata Penjualan Pada Bulan Ini");
                            } else if (namaprogram.getText().toString().length() ==0){
                                namaprogram.setError("Isi Nama Program");
                            } else if (periodeprogram.getText().toString().length() ==0){
                                periodeprogram.setError("Isi Periode Program");
                            } else if (mekanismeprogram.getText().toString().length() ==0){
                                mekanismeprogram.setError("Isi Mekanisme Program");
                            } else if (targetprogram.getText().toString().length() ==0){
                                targetprogram.setError("Isi Target Program");
                            } else if (keterangan.getText().toString().length() ==0){
                                keterangan.setError("Keterangan");
                            } else {
                                pDialog = new SweetAlertDialog(draft_kompetitor.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setCancelable(false);
                                pDialog.setTitleText("Harap Menunggu");
                                pDialog.show();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                String szId = sharedPreferences.getString("szId", null);
                                String nik_baru = sharedPreferences.getString("nik_karyawan", null);

                                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_kompetitor",
                                        new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                pDialog.dismissWithAnimation();
                                                Intent intent = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                                                startActivity(intent);
                                                dialog.dismiss();

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
//                                    upload_image_npwp();

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

                                        params.put("nik_survey", nik_baru);
                                        params.put("kode_pelanggan", szId);
                                        params.put("brand", id_products);

                                        params.put("uom_brand", id_uom);
                                        params.put("volume_brand", id_volume);
                                        params.put("average_penjualan_before", MyNumberWatcher_3Digit.trimCommaOfString(bulanlalu.getText().toString()));

                                        params.put("average_penjualan_now", MyNumberWatcher_3Digit.trimCommaOfString(bulanini.getText().toString()));
                                        params.put("harga_beli", MyNumberWatcher_3Digit.trimCommaOfString(hargabeli.getText().toString()));
                                        params.put("harga_jual", MyNumberWatcher_3Digit.trimCommaOfString(hargajual.getText().toString()));

                                        params.put("nama_program", namaprogram.getText().toString().toUpperCase());
                                        params.put("periode_program_awal", convertFormat(mDateStart));
                                        params.put("periode_program_akhir", convertFormat(mDateEnd));

                                        params.put("mekanisme_program", mekanismeprogram.getText().toString().toUpperCase());
                                        params.put("target_program", MyNumberWatcher_3Digit.trimCommaOfString(targetprogram.getText().toString()));
                                        params.put("keterangan", keterangan.getText().toString());

                                        params.put("jenis_kiriman", id_kiriman);
                                        params.put("nama_kiriman", namapengirim.getText().toString().toUpperCase());
                                        params.put("produk_lain", produklain.getText().toString().toUpperCase());

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
                                RequestQueue requestQueue2 = Volley.newRequestQueue(draft_kompetitor.this);
                                requestQueue2.add(stringRequest2);
                            }

                        } else if(!nama_produk.getText().toString().contains("LAIN")){
                            if (ukuran_produk.getText().toString().length() ==0){
                                ukuran_produk.setError("Isi Ukuran Produk");
                            } else if (isi.getText().toString().length() ==0){
                                isi.setError("Isi Jumlah Per Box");
                            } else if (hargabeli.getText().toString().length() ==0){
                                hargabeli.setError("Isi Harga Beli");
                            } else if (hargajual.getText().toString().length() ==0){
                                hargajual.setError("Isi Harga Jual");
                            } else if (Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(estimasimargin.getText().toString())) < 0){
                                estimasimargin.setError("Harga Jual Harus Lebih Besar Daripada Harga Beli");
                            } else if (pilihpengirim.getText().toString().length() ==0){
                                pilihpengirim.setError("Pilih Pengirim");
                            } else if (namapengirim.getText().toString().length() ==0){
                                namapengirim.setError("Isi Nama Pengirim");
                            } else if (bulanlalu.getText().toString().length() ==0){
                                bulanlalu.setError("Isi Rata-rata Penjualan Pada Bulan Lalu");
                            } else if (bulanini.getText().toString().length() ==0){
                                bulanini.setError("Isi Rata-rata Penjualan Pada Bulan Ini");
                            } else if (namaprogram.getText().toString().length() ==0){
                                namaprogram.setError("Isi Nama Program");
                            } else if (periodeprogram.getText().toString().length() ==0){
                                periodeprogram.setError("Isi Periode Program");
                            } else if (mekanismeprogram.getText().toString().length() ==0){
                                mekanismeprogram.setError("Isi Mekanisme Program");
                            } else if (targetprogram.getText().toString().length() ==0){
                                targetprogram.setError("Isi Target Program");
                            } else if (keterangan.getText().toString().length() ==0){
                                keterangan.setError("Keterangan");
                            } else {
                                pDialog = new SweetAlertDialog(draft_kompetitor.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setCancelable(false);
                                pDialog.setTitleText("Harap Menunggu");
                                pDialog.show();
                                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                                String szId = sharedPreferences.getString("szId", null);
                                String nik_baru = sharedPreferences.getString("nik_karyawan", null);

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_comparator?kode_pelanggan=" + szId + "&brand=" + id_products + "&uom_brand=" + id_uom,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                pDialog.dismissWithAnimation();
                                                new SweetAlertDialog(draft_kompetitor.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setContentText("Data Sudah Ada")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_kompetitor",
                                                        new Response.Listener<String>() {

                                                            @Override
                                                            public void onResponse(String response) {
                                                                pDialog.dismissWithAnimation();
                                                                Intent intent = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                                                                startActivity(intent);
                                                                dialog.dismiss();

                                                            }
                                                        }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
//                                    upload_image_npwp();

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

                                                        params.put("nik_survey", nik_baru);
                                                        params.put("kode_pelanggan", szId);
                                                        params.put("brand", id_products);

                                                        params.put("uom_brand", id_uom);
                                                        params.put("volume_brand", id_volume);
                                                        params.put("average_penjualan_before", MyNumberWatcher_3Digit.trimCommaOfString(bulanlalu.getText().toString()));

                                                        params.put("average_penjualan_now", MyNumberWatcher_3Digit.trimCommaOfString(bulanini.getText().toString()));
                                                        params.put("harga_beli", MyNumberWatcher_3Digit.trimCommaOfString(hargabeli.getText().toString()));
                                                        params.put("harga_jual", MyNumberWatcher_3Digit.trimCommaOfString(hargajual.getText().toString()));

                                                        params.put("nama_program", namaprogram.getText().toString().toUpperCase());
                                                        params.put("periode_program_awal", convertFormat(mDateStart));
                                                        params.put("periode_program_akhir", convertFormat(mDateEnd));

                                                        params.put("mekanisme_program", mekanismeprogram.getText().toString().toUpperCase());
                                                        params.put("target_program", MyNumberWatcher_3Digit.trimCommaOfString(targetprogram.getText().toString()));
                                                        params.put("keterangan", keterangan.getText().toString());

                                                        params.put("jenis_kiriman", id_kiriman);
                                                        params.put("nama_kiriman", namapengirim.getText().toString().toUpperCase());
                                                        params.put("produk_lain", "");

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
                                                RequestQueue requestQueue2 = Volley.newRequestQueue(draft_kompetitor.this);
                                                requestQueue2.add(stringRequest2);

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
                                };
                                stringRequest.setRetryPolicy(
                                        new DefaultRetryPolicy(
                                                500000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                        ));
                                RequestQueue requestQueue = Volley.newRequestQueue(draft_kompetitor.this);
                                requestQueue.add(stringRequest);
                            }
                        }

//
//
//
//
//                        }
                    }
                });

                hargabeli.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(hargajual.getText().toString().length() ==0 ){
                            estimasimargin.setText("0");
                        } else if(hargabeli.getText().toString().length() ==0 ){
                            estimasimargin.setText("0");
                        } else {
                            int jual = Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(hargajual.getText().toString()));
                            int beli = Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(hargabeli.getText().toString()));
                            estimasimargin.setText(String.valueOf(jual - beli));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                hargajual.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(hargabeli.getText().toString().length() ==0 ){
                            estimasimargin.setText("0");
                        } else if(hargajual.getText().toString().length() ==0 ){
                            estimasimargin.setText("0");
                        } else {
                            int jual = Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(hargajual.getText().toString()));
                            int beli = Integer.parseInt(MyNumberWatcher_3Digit.trimCommaOfString(hargabeli.getText().toString()));
                            estimasimargin.setText(String.valueOf(jual - beli));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().build();

                periodeprogram.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getSupportFragmentManager(), "Tag_picker");
                        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                Pair selectedDates = (Pair) materialDatePicker.getSelection();
                                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                                Date startDate = rangeDate.first;
                                Date endDate = rangeDate.second;
                                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd MMMM yyyy");
                                periodeprogram.setText(simpleFormat.format(startDate) + " - " + simpleFormat.format(endDate));

                                mDateStart = simpleFormat.format(startDate);
                                mDateEnd = simpleFormat.format(endDate);
                            }
                        });
                    }
                });



                StringRequest namaproduk = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_kompetitor",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String id = jsonObject1.getString("id");
                                            String kompetitor = jsonObject1.getString("kompetitor");
                                            products.add(kompetitor);
                                            idproducts.add(id);

                                        }
                                    }
                                    nama_produk.setAdapter(new ArrayAdapter<String>(draft_kompetitor.this, android.R.layout.simple_expandable_list_item_1, products));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                namaproduk.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestnamaproduk = Volley.newRequestQueue(draft_kompetitor.this);
                requestnamaproduk.add(namaproduk);

                StringRequest ukuranproduk = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_volume",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String id = jsonObject1.getString("id");
                                            String kompetitor = jsonObject1.getString("volume");
                                            volume.add(kompetitor);
                                            idvolume.add(id);
                                        }
                                    }
                                    isi.setAdapter(new ArrayAdapter<String>(draft_kompetitor.this, android.R.layout.simple_expandable_list_item_1, volume));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                ukuranproduk.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestukuranproduk = Volley.newRequestQueue(draft_kompetitor.this);
                requestukuranproduk.add(ukuranproduk);

                StringRequest uomproduk = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_uom",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String id = jsonObject1.getString("id");
                                            String kompetitor = jsonObject1.getString("uom");
                                            uom.add(kompetitor);
                                            iduom.add(id);
                                        }
                                    }
                                    ukuran_produk.setAdapter(new ArrayAdapter<String>(draft_kompetitor.this, android.R.layout.simple_expandable_list_item_1, uom));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                uomproduk.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestuomproduk = Volley.newRequestQueue(draft_kompetitor.this);
                requestuomproduk.add(uomproduk);


                StringRequest kirimans = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_kiriman",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String id = jsonObject1.getString("id");
                                            String kompetitor = jsonObject1.getString("nama_jenis");
                                            kiriman.add(kompetitor);
                                            idkiriman.add(id);

                                        }
                                    }
                                    pilihpengirim.setAdapter(new ArrayAdapter<String>(draft_kompetitor.this, android.R.layout.simple_expandable_list_item_1, kiriman));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                kirimans.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestnkirimans = Volley.newRequestQueue(draft_kompetitor.this);
                requestnkirimans.add(kirimans);

                dialog.show();
            }
        });
    }
    private void getDataSiapSimpan() {
        pDialog = new SweetAlertDialog(draft_kompetitor.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.dismissWithAnimation();
                Intent intent = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                startActivity(intent);
            }
        }, 5000);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_siap_simpan?kode_pelanggan="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                peringatan.setVisibility(View.GONE);
                                draft.setVisibility(View.VISIBLE);
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                updateData(movieObject.getString("id"));


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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateData(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_draft_simpan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", id);




                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        RequestQueue requestQueue = Volley.newRequestQueue(draft_kompetitor.this);
        requestQueue.add(stringRequest);
    }

    private void getDraftKompetitor() {
        pDialog = new SweetAlertDialog(draft_kompetitor.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        draftkompetitorPojoList.clear();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_draft?kode_pelanggan="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                peringatan.setVisibility(View.GONE);
                                draft.setVisibility(View.VISIBLE);
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final draftkompetitor_pojo movieItem = new draftkompetitor_pojo(
                                        movieObject.getString("id"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("nik_survey"),
                                        movieObject.getString("kode_pelanggan"),
                                        movieObject.getString("nama_jenis"),
                                        movieObject.getString("nama_kiriman"),
                                        movieObject.getString("produk_lain"),
                                        movieObject.getString("kompetitor"),
                                        movieObject.getString("uom"),
                                        movieObject.getString("volume"),
                                        movieObject.getString("average_penjualan_before"),
                                        movieObject.getString("average_penjualan_now"),
                                        movieObject.getString("harga_beli"),
                                        movieObject.getString("harga_jual"),
                                        movieObject.getString("nama_program"),
                                        movieObject.getString("periode_program_awal"),
                                        movieObject.getString("periode_program_akhir"),
                                        movieObject.getString("mekanisme_program"),
                                        movieObject.getString("target_program"),
                                        movieObject.getString("keterangan"));
                                draftkompetitorPojoList.add(movieItem);

                            }
                            adapterDraftKompetitor = new ListViewAdapterDraftKompetitor(draftkompetitorPojoList, getApplicationContext());
                            draft.setAdapter(adapterDraftKompetitor);
                            adapterDraftKompetitor.notifyDataSetChanged();
                            pDialog.dismissWithAnimation();
                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        peringatan.setVisibility(View.VISIBLE);
                        draft.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return convetDateFormat.format(date);
    }

    public class ListViewAdapterDraftKompetitor extends ArrayAdapter<draftkompetitor_pojo> {

        private class ViewHolder {
            TextView nama_produk;
            ListView sub_list;
            List<draftkompetitor_pojo> sub;
            ListViewAdapterSubDraft adapterSubDraft;

        }
        List<draftkompetitor_pojo> draftkompetitorPojoList;
        private Context context;

        public ListViewAdapterDraftKompetitor(List<draftkompetitor_pojo> draftkompetitorPojoList, Context context) {
            super(context, R.layout.list_item_draft_kompetitor, draftkompetitorPojoList);
            this.draftkompetitorPojoList = draftkompetitorPojoList;
            this.context = context;

        }

        public int getCount() {
            return draftkompetitorPojoList.size();
        }

        public draftkompetitor_pojo getItem(int position) {
            return draftkompetitorPojoList.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            draftkompetitor_pojo draftkompetitor_pojo = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_draft_kompetitor, parent, false);
                viewHolder.nama_produk = (TextView) convertView.findViewById(R.id.nama_produk);
                viewHolder.sub_list = (ListView) convertView.findViewById(R.id.sub_list);

                viewHolder.sub = new ArrayList<>();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String szId = sharedPreferences.getString("szId", null);
            viewHolder.sub.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_draft_sub?kode_pelanggan="+ szId + "&nama_product=" + draftkompetitor_pojo.getKompetitor(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                final JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    final draftkompetitor_pojo movieItem = new draftkompetitor_pojo(
                                            movieObject.getString("id"),
                                            movieObject.getString("submit_date"),
                                            movieObject.getString("nik_survey"),
                                            movieObject.getString("kode_pelanggan"),
                                            movieObject.getString("nama_jenis"),
                                            movieObject.getString("nama_kiriman"),
                                            movieObject.getString("produk_lain"),
                                            movieObject.getString("kompetitor"),
                                            movieObject.getString("uom"),
                                            movieObject.getString("volume"),
                                            movieObject.getString("average_penjualan_before"),
                                            movieObject.getString("average_penjualan_now"),
                                            movieObject.getString("harga_beli"),
                                            movieObject.getString("harga_jual"),
                                            movieObject.getString("nama_program"),
                                            movieObject.getString("periode_program_awal"),
                                            movieObject.getString("periode_program_akhir"),
                                            movieObject.getString("mekanisme_program"),
                                            movieObject.getString("target_program"),
                                            movieObject.getString("keterangan"));
                                    viewHolder.sub.add(movieItem);

                                }

                                viewHolder.adapterSubDraft = new ListViewAdapterSubDraft(viewHolder.sub, getApplicationContext());
                                viewHolder.sub_list.setAdapter(viewHolder.adapterSubDraft);
                                viewHolder.adapterSubDraft.notifyDataSetChanged();

                                listadd.setListViewHeightBasedOnChildren(viewHolder.sub_list);
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
            RequestQueue requestQueue = Volley.newRequestQueue(draft_kompetitor.this);
            requestQueue.add(stringRequest);

            viewHolder.nama_produk.setText(draftkompetitor_pojo.getKompetitor());



            return convertView;

        }
    }

    public class ListViewAdapterSubDraft extends ArrayAdapter<draftkompetitor_pojo> {

        private class ViewHolder {
            TextView volume, uom;
            TextView qty, harga;
            ImageView hapus, crate;


        }
        List<draftkompetitor_pojo> draftkompetitorPojoList;
        private Context context;

        public ListViewAdapterSubDraft(List<draftkompetitor_pojo> draftkompetitorPojoList, Context context) {
            super(context, R.layout.list_item_sub_draft, draftkompetitorPojoList);
            this.draftkompetitorPojoList = draftkompetitorPojoList;
            this.context = context;

        }

        public int getCount() {
            return draftkompetitorPojoList.size();
        }

        public draftkompetitor_pojo getItem(int position) {
            return draftkompetitorPojoList.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            draftkompetitor_pojo draftkompetitor_pojo = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_sub_draft, parent, false);

                viewHolder.volume = (TextView) convertView.findViewById(R.id.volume);
                viewHolder.uom = (TextView) convertView.findViewById(R.id.uom);

                viewHolder.qty = (TextView) convertView.findViewById(R.id.qty);
                viewHolder.harga = (TextView) convertView.findViewById(R.id.harga);
                viewHolder.hapus = (ImageView) convertView.findViewById(R.id.hapus);
                viewHolder.crate = (ImageView) convertView.findViewById(R.id.crate);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(draft_kompetitor.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Apakah anda yakin?")
                            .setConfirmText("Ya")
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    pDialog = new SweetAlertDialog(draft_kompetitor.this, SweetAlertDialog.PROGRESS_TYPE);
                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    pDialog.setCancelable(false);
                                    pDialog.setTitleText("Harap Menunggu");
                                    pDialog.show();
                                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_draft_hapus",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    pDialog.dismissWithAnimation();
                                                    Intent intent = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                                                    startActivity(intent);
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                }
                                            }
                                    ) {

                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            HashMap<String, String> params = new HashMap<String, String>();
                                            String creds = String.format("%s:%s", "admin", "Databa53");
                                            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                            params.put("Authorization", auth);
                                            return params;
                                        }
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();

                                            params.put("id", draftkompetitor_pojo.getId());




                                            return params;
                                        }

                                    };
                                    stringRequest.setRetryPolicy(
                                            new DefaultRetryPolicy(
                                                    500000,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                            )
                                    );

                                    RequestQueue requestQueue = Volley.newRequestQueue(draft_kompetitor.this);
                                    requestQueue.add(stringRequest);
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();

                }
            });

            int harga = Integer.parseInt(draftkompetitor_pojo.getHarga_beli());
            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);




            if(draftkompetitor_pojo.getUom().contains("Gallon")){
                viewHolder.volume.setText(draftkompetitor_pojo.getVolume() + "/Gallon");
                viewHolder.crate.setImageResource(R.drawable.ic_galon);
            } else {
                viewHolder.volume.setText(draftkompetitor_pojo.getVolume() + "/Box");
            }

            if(draftkompetitor_pojo.getProduk_lain().equals("") || draftkompetitor_pojo.getProduk_lain().equals("null")){
                viewHolder.uom.setText(draftkompetitor_pojo.getUom());
            } else {
                viewHolder.uom.setText(draftkompetitor_pojo.getProduk_lain() + " " + draftkompetitor_pojo.getUom());
            }

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String average_penjualan = formatter.format(Integer.parseInt(draftkompetitor_pojo.getAverage_penjualan_now()));


            String penjualan = average_penjualan.replace(",", ".");

            viewHolder.qty.setText(penjualan + " Item");
            viewHolder.harga.setText(String.valueOf(kursIndonesia.format(harga)));



            return convertView;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDraftKompetitor();

    }


}