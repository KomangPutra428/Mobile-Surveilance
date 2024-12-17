package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tvip.surveylance.pojo.kompetitor_pojo;
import com.tvip.surveylance.pojo.produk_lain_pojo;
import com.tvip.surveylance.pojo.tren_pojo;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class tren_penjualan extends AppCompatActivity {
    TextView tambah;
    SharedPreferences sharedPreferences;
    List<tren_pojo> tren_pojoList = new ArrayList<>();
    ListView penjualan;
    ListViewAdapterTrenPenjualan adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tren_penjualan);
        HttpsTrustManager.allowAllSSL();
        tambah = findViewById(R.id.tambah);
        penjualan = findViewById(R.id.penjualan);
        
        listtrenpenjualan();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(tren_penjualan.this);
                dialog.setContentView(R.layout.isi_tren);

                Button batal = dialog.findViewById(R.id.batal);
                Button tambahkan = dialog.findViewById(R.id.tambahkan);

                final EditText editproduk = dialog.findViewById(R.id.editproduk);
                final EditText editbulan = dialog.findViewById(R.id.editbulan);
                final EditText editqty = dialog.findViewById(R.id.editqty);

                editqty.setText("0");
                ImageButton minus = dialog.findViewById(R.id.minus);
                ImageButton add = dialog.findViewById(R.id.add);
                final int[] count2 = new int[1];

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count2[0]++;
                        editqty.setText(String.valueOf(count2[0]));
                    }

                });
                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editqty.setText(String.valueOf(count2[0]));
                        if (count2[0] == 0) {
                            return;
                        }
                        count2[0]--;
                    }
                });


                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                editbulan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar today = Calendar.getInstance();
                        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(tren_penjualan.this,
                                new MonthPickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(int selectedMonth  , int selectedYear) {
                                        editbulan.setText(convertFormat((selectedMonth + 1) + " - " + selectedYear));
                                    }
                                    },
                                today.get(Calendar.YEAR), today.get(Calendar.MONTH));
                        builder.setActivatedMonth(Calendar.JULY)
                                .setMinYear(1990)
                                .setActivatedYear(today.get(Calendar.YEAR))
                                .setMaxYear(2030)
                                .setMinMonth(Calendar.JANUARY)
                                .setTitle("Pilih Bulan")
                                .setMaxMonth(Calendar.DECEMBER)
                                .build().show();
                    }
                });

                tambahkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editproduk.getText().toString().length() == 0){
                            editproduk.setError("Isi Nama Produk");
                        } else if (editbulan.getText().toString().length() == 0){
                            editbulan.setError("Bulan");
                        } else if (editqty.getText().toString().length() == 0){
                            editqty.setError("Isi QTY");
                        } else {
                            final SweetAlertDialog pDialog = new SweetAlertDialog(tren_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setCancelable(false);
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.show();

                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_tren",
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.dismissWithAnimation();
                                            SweetAlertDialog success = new SweetAlertDialog(tren_penjualan.this, SweetAlertDialog.SUCCESS_TYPE);
                                            success.setContentText("Berhasil disimpan");
                                            success.setCancelable(false);
                                            success.setConfirmText("OK");
                                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    dialog.dismiss();
                                                    listtrenpenjualan();
                                                }
                                            });
                                            success.show();

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

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
                                    String szId = sharedPreferences.getString("szId", null);

                                    params.put("szId", szId);
                                    params.put("sku", editproduk.getText().toString());
                                    params.put("qty", editqty.getText().toString());
                                    params.put("bulan", bulan(editbulan.getText().toString()));
                                    params.put("tahun", tahun(editbulan.getText().toString()));

                                    params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                                    params.put("longitude", MainActivity.longitude);
                                    params.put("latitude", MainActivity.latitude);

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(tren_penjualan.this);
                            requestQueue2.add(stringRequest2);


                        }
                    }
                });

                dialog.show();
            }
        });
    }

    private void listtrenpenjualan() {
        tren_pojoList.clear();
        final SweetAlertDialog pDialog = new SweetAlertDialog(tren_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        pDialog.setCancelable(false);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_tren?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final tren_pojo movieItem = new tren_pojo(
                                        movieObject.getString("sku"),
                                        movieObject.getString("qty"),
                                        movieObject.getString("bulan"),
                                        movieObject.getString("tahun"));
                                tren_pojoList.add(movieItem);

                            }
                            adapter = new ListViewAdapterTrenPenjualan(tren_pojoList, getApplicationContext());
                            penjualan.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM - yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("MMMM yyyy");
        return convetDateFormat.format(date);
    }

    public static String bulan(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("MM");
        return convetDateFormat.format(date);
    }

    public static String tahun(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy");
        return convetDateFormat.format(date);
    }

    public static class ListViewAdapterTrenPenjualan extends ArrayAdapter<tren_pojo> {

        List<tren_pojo> tren_pojos;

        private Context context;

        public ListViewAdapterTrenPenjualan(List<tren_pojo> tren_pojos, Context context) {
            super(context, R.layout.list_item_tren, tren_pojos);
            this.tren_pojos = tren_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_tren, null, true);


            TextView namaproduk = listViewItem.findViewById(R.id.namaproduk);
            TextView tanggal = listViewItem.findViewById(R.id.tanggal);
            TextView qty = listViewItem.findViewById(R.id.qty);

            tren_pojo tren_pojo = getItem(position);

            namaproduk.setText(tren_pojo.getSku());
            tanggal.setText(convertFormat(tren_pojo.getBulan() + " - " + tren_pojo.getTahun()));
            qty.setText(tren_pojo.getQty() + " Qty");

            return listViewItem;
        }
    }
}