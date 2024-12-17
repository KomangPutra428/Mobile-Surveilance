package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tvip.surveylance.pojo.pelanggan_pojo_2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class total_pelanggan extends AppCompatActivity {
    TextView pelanggan, tambah;
    ListView listpelanggan;
    List<pelanggan_pojo_2> pelangganPojos = new ArrayList<>();
    SharedPreferences sharedPreferences;
    ListViewAdapterPelanggans adapter;
    SweetAlertDialog pDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_pelanggan);
        HttpsTrustManager.allowAllSSL();
        pelanggan = findViewById(R.id.pelanggan);
        listpelanggan = findViewById(R.id.listpelanggan);
        tambah = findViewById(R.id.tambah);

        listpelanggan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
                final Dialog dialog = new Dialog(total_pelanggan.this);
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
                            pDialog = new SweetAlertDialog(total_pelanggan.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setCancelable(false);
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_pelanggan",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.cancel();
                                            dialog.dismiss();
                                            SweetAlertDialog success = new SweetAlertDialog(total_pelanggan.this, SweetAlertDialog.SUCCESS_TYPE);
                                            success.setContentText("Berhasil disimpan");
                                            success.setCancelable(false);
                                            success.setConfirmText("OK");
                                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    getPelanggan();
                                                }
                                            });
                                            success.show();

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pDialog.cancel();
                                            dialog.dismiss();
                                            SweetAlertDialog success = new SweetAlertDialog(total_pelanggan.this, SweetAlertDialog.SUCCESS_TYPE);
                                            success.setContentText("Berhasil disimpan");
                                            success.setCancelable(false);
                                            success.setConfirmText("OK");
                                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    getPelanggan();
                                                }
                                            });
                                            success.show();

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

                                    params.put("id", adapter.getItem(position).getId());
                                    params.put("nama_pelanggan", editnama.getText().toString());
                                    params.put("alamat_pelanggan", editalamat.getText().toString());

                                    params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                                    params.put("longitude", MainActivity.longitude);
                                    params.put("latitude", MainActivity.latitude);


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

                            RequestQueue requestQueue = Volley.newRequestQueue(total_pelanggan.this);
                            requestQueue.add(stringRequest);
                        }

                    }
                });
                dialog.show();
            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(total_pelanggan.this);
                dialog.setContentView(R.layout.add_pelanggan);

                final EditText editnama = dialog.findViewById(R.id.editnama);
                final EditText editalamat = dialog.findViewById(R.id.editalamat);
                final AutoCompleteTextView editjenis = dialog.findViewById(R.id.editjenis);

                Button batal = dialog.findViewById(R.id.batal);
                Button tambahkan = dialog.findViewById(R.id.tambahkan);

                final ArrayList<String> Armada = new ArrayList<>();

                final int[] nomor = new int[1];
                StringRequest karyawanlist = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_jenis_pelanggan",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String karyawan = null;
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            karyawan = jsonObject1.getString("jenis_pelanggan");
                                            Armada.add(karyawan);

                                        }
                                    }
                                    editjenis.setAdapter(new ArrayAdapter<String>(total_pelanggan.this, android.R.layout.simple_expandable_list_item_1, Armada));
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
                karyawanlist.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(total_pelanggan.this);
                requestQueue.add(karyawanlist);
                
                editjenis.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            editjenis.showDropDown();
                        }
                    }
                });
                editjenis.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        editjenis.showDropDown();
                        return false;
                    }
                });

                editjenis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        nomor[0] = position + 1;
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
                        editnama.setError(null);
                        editalamat.setError(null);
                        editjenis.setError(null);
                        if(editnama.getText().toString().length() == 0){
                            editnama.setError("Masukkan Nama Toko");
                        } else if(editalamat.getText().toString().length() == 0){
                            editalamat.setError("Masukkan Alamat Toko");
                        } else if(editjenis.getText().toString().length() == 0){
                            editjenis.setError("Pilih Jenis");
                        } else {
                            pDialog = new SweetAlertDialog(total_pelanggan.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setCancelable(false);
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.show();
                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_new_pelanggan",
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
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

                                                                        if(editjenis.getText().toString().equals("Rumahan")){
                                                                            updatetotal(nomor[0], Integer.parseInt(movieObject.getString("rumahan")));
                                                                        } else if(editjenis.getText().toString().equals("Kantoran")){
                                                                            updatetotal(nomor[0], Integer.parseInt(movieObject.getString("kantoran")));
                                                                        } else {
                                                                            updatetotal(nomor[0], Integer.parseInt(movieObject.getString("outlet")));
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
                                            RequestQueue requestQueue = Volley.newRequestQueue(total_pelanggan.this);
                                            requestQueue.add(stringRequest);


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
                                    params.put("nama_pelanggan", editnama.getText().toString());
                                    params.put("alamat_pelanggan", editalamat.getText().toString());
                                    params.put("jenis", String.valueOf(nomor[0]));

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(total_pelanggan.this);
                            requestQueue2.add(stringRequest2);
                        }

                    }
                });
                dialog.show();
            }
        });

        getPelanggan();
    }

    private void updatetotal(final int i, final int s) {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_UpdateTotal",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.cancel();
                        dialog.dismiss();
                        SweetAlertDialog success = new SweetAlertDialog(total_pelanggan.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setContentText("Berhasil disimpan");
                        success.setCancelable(false);
                        success.setConfirmText("OK");
                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                getPelanggan();
                            }
                        });
                        success.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.cancel();
                        dialog.dismiss();
                        SweetAlertDialog success = new SweetAlertDialog(total_pelanggan.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setContentText("Berhasil disimpan");
                        success.setCancelable(false);
                        success.setConfirmText("OK");
                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                getPelanggan();
                            }
                        });
                        success.show();

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
                String id = sharedPreferences.getString("szId", null);


                params.put("szId", id);
                params.put("jenis_pelanggan", String.valueOf(i));
                params.put("total", String.valueOf(s + 1));

                params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                params.put("longitude", MainActivity.longitude);
                params.put("latitude", MainActivity.latitude);


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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getPelanggan() {
        pelangganPojos.clear();
        final SweetAlertDialog pDialog = new SweetAlertDialog(total_pelanggan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        HttpsTrustManager.allowAllSSL();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_datapelanggan?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final pelanggan_pojo_2 movieItem = new pelanggan_pojo_2(
                                        movieObject.getString("id"),
                                        movieObject.getString("nama_pelanggan"),
                                        movieObject.getString("alamat_pelanggan"),
                                        movieObject.getString("jenis"));
                                pelangganPojos.add(movieItem);

                            }
                            adapter = new ListViewAdapterPelanggans(pelangganPojos, getApplicationContext());
                            listpelanggan.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            pDialog.dismissWithAnimation();

                            pelanggan.setText(String.valueOf(pelangganPojos.size()) + " Pelanggan");
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

    public static class ListViewAdapterPelanggans extends ArrayAdapter<pelanggan_pojo_2> {

        private class ViewHolder {
            TextView namatoko;
            TextView nama, alamat, ubah;

        }
        List<pelanggan_pojo_2> pelanggan_outlets;
        private Context context;

        public ListViewAdapterPelanggans(List<pelanggan_pojo_2> pelanggan_outlets, Context context) {
            super(context, R.layout.list_item_pelanggan_owner, pelanggan_outlets);
            this.pelanggan_outlets = pelanggan_outlets;
            this.context = context;

        }

        public int getCount() {
            return pelanggan_outlets.size();
        }

        public pelanggan_pojo_2 getItem(int position) {
            return pelanggan_outlets.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            pelanggan_pojo_2 pelanggan_pojo_2 = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_pelanggan_owner, parent, false);

                viewHolder.namatoko = (TextView) convertView.findViewById(R.id.namatoko);
                viewHolder.nama = (TextView) convertView.findViewById(R.id.nama);
                viewHolder.alamat = (TextView) convertView.findViewById(R.id.alamat);
                viewHolder.ubah = (TextView) convertView.findViewById(R.id.ubah);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.namatoko.setText(MainActivity.txt_nama.getText().toString());
            viewHolder.nama.setText(pelanggan_pojo_2.getNama_pelanggan());
            viewHolder.alamat.setText(pelanggan_pojo_2.getAlamat_pelanggan());



            return convertView;

        }
    }
}