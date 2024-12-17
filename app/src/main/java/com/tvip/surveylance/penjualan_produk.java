package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tvip.surveylance.pojo.produk_lain_pojo;
import com.tvip.surveylance.pojo.total_pelanggan_pojo;
import com.tvip.surveylance.pojo.tren_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class penjualan_produk extends AppCompatActivity {
    TextView coverage, kontribusi;
    SharedPreferences sharedPreferences;
    ListView totalpelanggan, totalproduklain, produktersedialist, amdklist;
    List<total_pelanggan_pojo> totalPelangganPojoList = new ArrayList<>();
    List<produk_lain_pojo> produk_lain_pojos = new ArrayList<>();
    ArrayList<String> produktersedia = new ArrayList<>();
    ArrayList<String> amdk = new ArrayList<>();


    ListViewAdapterTotalPelanggan adapter;
    ListViewAdapterProdukLainLain adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_produk);
        HttpsTrustManager.allowAllSSL();
        coverage = findViewById(R.id.coverage);
        kontribusi = findViewById(R.id.kontribusi);
        totalpelanggan = findViewById(R.id.totalpelanggan);
        totalproduklain = findViewById(R.id.totalproduklain);
        produktersedialist = findViewById(R.id.produktersedialist);
        amdklist = findViewById(R.id.amdklist);

        coverage.setText(getIntent().getStringExtra("area"));

        getPelanggan();
        getProdukList();
        getKontribusi();
        getProdukTersedia();
        getamdk();
    }

    private void getamdk() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Amdk?szId=" + szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String city = jsonObject1.getString("jenis_amdk");
                                    amdk.add(city);

                                }
                            }
                            amdklist.setAdapter(new ArrayAdapter<String>(penjualan_produk.this, R.layout.simple_list_item, R.id.text, amdk));
                            listchanger.setListViewHeightBasedOnChildren(amdklist);
                        } catch (JSONException e) {
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
        kecamatan.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(penjualan_produk.this);
        requestkota.add(kecamatan);
    }

    private void getProdukTersedia() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_ProdukTersedia?szId=" + szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String city = jsonObject1.getString("jenis_produk");
                                    produktersedia.add(city);

                                }
                            }
                            produktersedialist.setAdapter(new ArrayAdapter<String>(penjualan_produk.this, R.layout.simple_list_item, R.id.text, produktersedia));
                            listchanger.setListViewHeightBasedOnChildren(produktersedialist);
                        } catch (JSONException e) {
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
        kecamatan.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(penjualan_produk.this);
        requestkota.add(kecamatan);
    }

    private void getKontribusi() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_kontribusi?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                kontribusi.setText(movieObject.getString("jenis_persentase"));

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

    private void getProdukList() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produklain?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final produk_lain_pojo movieItem = new produk_lain_pojo(
                                        movieObject.getString("produk"),
                                        movieObject.getString("omset"));
                                produk_lain_pojos.add(movieItem);

                            }
                            adapter2 = new ListViewAdapterProdukLainLain(produk_lain_pojos, getApplicationContext());
                            totalproduklain.setAdapter(adapter2);
                            adapter2.notifyDataSetChanged();
                            listchanger.setListViewHeightBasedOnChildren(totalproduklain);

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

    private void getPelanggan() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(penjualan_produk.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        pDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_total_pelanggan?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final total_pelanggan_pojo movieItem = new total_pelanggan_pojo(
                                        movieObject.getString("jenis_pelanggan"),
                                        movieObject.getString("total"));
                                totalPelangganPojoList.add(movieItem);

                            }
                            adapter = new ListViewAdapterTotalPelanggan(totalPelangganPojoList, getApplicationContext());
                            totalpelanggan.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            listchanger.setListViewHeightBasedOnChildren(totalpelanggan);

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

    public static class ListViewAdapterProdukLainLain extends ArrayAdapter<produk_lain_pojo> {

        List<produk_lain_pojo> produk_lain_pojos;

        private Context context;

        public ListViewAdapterProdukLainLain(List<produk_lain_pojo> produk_lain_pojos, Context context) {
            super(context, R.layout.list_item_total_pelanggan, produk_lain_pojos);
            this.produk_lain_pojos = produk_lain_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_total_pelanggan, null, true);


            TextView jenis = listViewItem.findViewById(R.id.jenis);
            TextView total = listViewItem.findViewById(R.id.total);

            produk_lain_pojo produkLainPojo = getItem(position);

            jenis.setText(produkLainPojo.getProduk());
            String str = NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(produkLainPojo.getOmset()));
            total.setText("Rp. " + str);

            return listViewItem;
        }
    }

    public static class ListViewAdapterTotalPelanggan extends ArrayAdapter<total_pelanggan_pojo> {

        List<total_pelanggan_pojo> total_pelanggan_pojos;

        private Context context;

        public ListViewAdapterTotalPelanggan(List<total_pelanggan_pojo> total_pelanggan_pojos, Context context) {
            super(context, R.layout.list_item_total_pelanggan, total_pelanggan_pojos);
            this.total_pelanggan_pojos = total_pelanggan_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_total_pelanggan, null, true);


            TextView jenis = listViewItem.findViewById(R.id.jenis);
            TextView total = listViewItem.findViewById(R.id.total);

            total_pelanggan_pojo total_pelanggan_pojo = getItem(position);

            jenis.setText(total_pelanggan_pojo.getJenis_pelanggan());
            total.setText(total_pelanggan_pojo.getTotal());

            return listViewItem;
        }
    }
}