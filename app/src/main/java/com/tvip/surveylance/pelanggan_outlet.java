package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tvip.surveylance.pojo.pelanggan_pojo;
import com.tvip.surveylance.pojo.produk_lain_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class pelanggan_outlet extends AppCompatActivity {
    TextView pelanggan;
    ListView pelangganlist;
    SharedPreferences sharedPreferences;
    ListViewAdapterPelanggan adapter;
    List<pelanggan_pojo> pelanggan_pojos = new ArrayList<>();
    Button batal, simpan;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan_outlet);
        HttpsTrustManager.allowAllSSL();
        pelanggan = findViewById(R.id.pelanggan);
        pelangganlist = findViewById(R.id.pelangganlist);

        batal = findViewById(R.id.batal);
        simpan = findViewById(R.id.simpan);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog = new SweetAlertDialog(pelanggan_outlet.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setCancelable(false);
                pDialog.setTitleText("Harap Menunggu");
                pDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        postpelanggan();
                    }
                }, 5000);
            }
        });

        pelangganlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                final Dialog dialog = new Dialog(pelanggan_outlet.this);
                dialog.setContentView(R.layout.namaalamat_pelanggan);

                final EditText editnama = dialog.findViewById(R.id.editnama);
                final EditText editalamat = dialog.findViewById(R.id.editalamat);

                Button batal = dialog.findViewById(R.id.batal);
                Button tambahkan = dialog.findViewById(R.id.tambahkan);

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tambahkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editnama.setError(null);
                        editalamat.setError(null);
                        if(editnama.getText().toString().length() == 0){
                            editnama.setError("Masukkan Nama Toko");
                        } else if(editalamat.getText().toString().length() == 0){
                            editalamat.setError("Masukkan Alamat Toko");
                        } else {
                            adapter.getItem(position).setNama_pelanggan(editnama.getText().toString());
                            adapter.getItem(position).setAlamat_pelanggan(editalamat.getText().toString());
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                    }
                });
                dialog.show();
            }
        });

        HttpsTrustManager.allowAllSSL();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_count?szId=" + szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {

                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    pelanggan.setText(movieObject.getString("jumlah_total") + " Pelanggan");

                                    for (int x = 1; x <= Integer.parseInt(movieObject.getString("rumahan")); x++) {
                                        pelanggan_pojos.add(new pelanggan_pojo("", "", "1"));
                                        adapter = new ListViewAdapterPelanggan(pelanggan_pojos, pelanggan_outlet.this);
                                        pelangganlist.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }

                                    for (int y = 1; y <= Integer.parseInt(movieObject.getString("kantoran")); y++) {
                                        pelanggan_pojos.add(new pelanggan_pojo("", "", "2"));
                                        adapter = new ListViewAdapterPelanggan(pelanggan_pojos, pelanggan_outlet.this);
                                        pelangganlist.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }

                                    for (int z = 1; z <= Integer.parseInt(movieObject.getString("outlet")); z++) {
                                        pelanggan_pojos.add(new pelanggan_pojo("", "", "3"));
                                        adapter = new ListViewAdapterPelanggan(pelanggan_pojos, pelanggan_outlet.this);
                                        pelangganlist.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }


                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pelanggan.setText("0");
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
        RequestQueue requestQueue = Volley.newRequestQueue(pelanggan_outlet.this);
        requestQueue.add(stringRequest);

    }

    private void postpelanggan() {

        for (int i = 0; i <= pelanggan_pojos.size() - 1; i++) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_detailpelanggan",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            if(finalI == pelanggan_pojos.size() - 1){
                                pDialog.dismissWithAnimation();
                                SweetAlertDialog success = new SweetAlertDialog(pelanggan_outlet.this, SweetAlertDialog.SUCCESS_TYPE);
                                success.setContentText("Berhasil disimpan");
                                success.setCancelable(false);
                                success.setConfirmText("OK");
                                success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent i = new Intent(getApplicationContext(), menu_survey.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);
                                    }
                                });
                                success.show();
                            }

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
                    params.put("nama_pelanggan", adapter.getItem(finalI).getNama_pelanggan());
                    params.put("alamat_pelanggan", adapter.getItem(finalI).getAlamat_pelanggan());
                    params.put("jenis", adapter.getItem(finalI).getJenis());

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(pelanggan_outlet.this);
            requestQueue2.add(stringRequest2);
        }
    }

    public static class ListViewAdapterPelanggan extends ArrayAdapter<pelanggan_pojo> {

        private class ViewHolder {
            TextView namatoko;
            TextView nama, ketnama;
            TextView alamat, ketalamat;
            TextView ubah;

        }
        List<pelanggan_pojo> pelanggan_outlets;
        private Context context;

        public ListViewAdapterPelanggan(List<pelanggan_pojo> pelanggan_outlets, Context context) {
            super(context, R.layout.list_item_pelanggan_outlet, pelanggan_outlets);
            this.pelanggan_outlets = pelanggan_outlets;
            this.context = context;

        }

        public int getCount() {
            return pelanggan_outlets.size();
        }

        public pelanggan_pojo getItem(int position) {
            return pelanggan_outlets.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            pelanggan_pojo pelanggan_pojo = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_pelanggan_outlet, parent, false);

                viewHolder.namatoko = (TextView) convertView.findViewById(R.id.namatoko);
                viewHolder.nama = (TextView) convertView.findViewById(R.id.nama);
                viewHolder.ketnama = (TextView) convertView.findViewById(R.id.ketnama);

                viewHolder.alamat = (TextView) convertView.findViewById(R.id.alamat);
                viewHolder.ketalamat = (TextView) convertView.findViewById(R.id.ketalamat);
                viewHolder.ubah = (TextView) convertView.findViewById(R.id.ubah);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.namatoko.setText(MainActivity.txt_nama.getText().toString());
            viewHolder.nama.setText(pelanggan_pojo.getNama_pelanggan());
            viewHolder.alamat.setText(pelanggan_pojo.getAlamat_pelanggan());

            viewHolder.nama.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(viewHolder.nama.getText().toString().length()!=0){
                        viewHolder.ketnama.setText("");
                        viewHolder.ubah.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(viewHolder.nama.getText().toString().length()!=0){
                        viewHolder.ketnama.setText("");
                        viewHolder.ubah.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(viewHolder.nama.getText().toString().length()!=0){
                        viewHolder.ketnama.setText("");
                        viewHolder.ubah.setVisibility(View.VISIBLE);
                    }
                }
            });

            viewHolder.alamat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(viewHolder.alamat.getText().toString().length()!=0){
                        viewHolder.ketalamat.setText("");
                        viewHolder.ubah.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(viewHolder.alamat.getText().toString().length()!=0){
                        viewHolder.ketalamat.setText("");
                        viewHolder.ubah.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(viewHolder.alamat.getText().toString().length()!=0){
                        viewHolder.ketalamat.setText("");
                        viewHolder.ubah.setVisibility(View.VISIBLE);
                    }
                }
            });


            return convertView;

        }
    }
}