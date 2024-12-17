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
import android.widget.ImageButton;
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
import com.google.android.material.textfield.TextInputLayout;
import com.tvip.surveylance.pojo.armada_pojo;
import com.tvip.surveylance.pojo.kompetitor_pojo;
import com.tvip.surveylance.pojo.memento_pojo;
import com.tvip.surveylance.pojo.produkkami_pojo;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class armada_operasional extends AppCompatActivity {
    TextView tambah;
    SharedPreferences sharedPreferences;
    ListView armada;
    List<armada_pojo> armada_pojoList = new ArrayList<>();
    ListViewAdapterKendaraan adapter;
    String id_JenisArmada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armada_operasional);
        HttpsTrustManager.allowAllSSL();
        tambah = findViewById(R.id.tambah);
        armada = findViewById(R.id.armada);

        armada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String kapasitas = ((armada_pojo) parent.getItemAtPosition(position)).getKapasitas();
                String ids = ((armada_pojo) parent.getItemAtPosition(position)).getId();

                if(kapasitas.equals("0")){

                    final Dialog dialog = new Dialog(armada_operasional.this);
                    dialog.setContentView(R.layout.armada);

                    TextInputLayout jenis = dialog.findViewById(R.id.jenis);

                    Button batal = dialog.findViewById(R.id.batal);
                    Button tambahkan = dialog.findViewById(R.id.tambahkan);

                    final AutoCompleteTextView editjenis = dialog.findViewById(R.id.editjenis);
                    final AutoCompleteTextView jeniskapasitas = dialog.findViewById(R.id.jeniskapasitas);
                    final EditText qty = dialog.findViewById(R.id.qty);
                    RelativeLayout tambahlayout = dialog.findViewById(R.id.tambahlayout);

                    editjenis.setVisibility(View.GONE);
                    tambahlayout.setVisibility(View.GONE);
                    jenis.setVisibility(View.GONE);

                    final ArrayList<String> IdJenisArmada = new ArrayList<>();
                    final ArrayList<String> JenisArmada = new ArrayList<>();

                    jeniskapasitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            id_JenisArmada = IdJenisArmada.get(position).toString();
                            jeniskapasitas.setError(null);

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
                          if (jeniskapasitas.getText().toString().length() == 0){
                                jeniskapasitas.setError("Isi Jenis Kapasitas");
                            } else if (qty.getText().toString().length() == 0){
                                qty.setError("Isi Qty");
                            } else {
                              final SweetAlertDialog[] pDialog = {new SweetAlertDialog(armada_operasional.this, SweetAlertDialog.PROGRESS_TYPE)};
                              pDialog[0].getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                              pDialog[0].setCancelable(false);
                              pDialog[0].setTitleText("Harap Menunggu");
                              pDialog[0].show();
                              StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_armada",
                                      new Response.Listener<String>() {

                                          @Override
                                          public void onResponse(String response) {
                                              pDialog[0].cancel();
                                              SweetAlertDialog success = new SweetAlertDialog(armada_operasional.this, SweetAlertDialog.SUCCESS_TYPE);
                                              success.setContentText("Berhasil disimpan");
                                              success.setCancelable(false);
                                              success.setConfirmText("OK");
                                              success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                  @Override
                                                  public void onClick(SweetAlertDialog sDialog) {
                                                      sDialog.dismissWithAnimation();
                                                      dialog.dismiss();
                                                      getKendaraan();

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

                                      params.put("id", ids);
                                      params.put("satuan", id_JenisArmada);
                                      params.put("kapasitas", qty.getText().toString());
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
                              RequestQueue requestQueue2 = Volley.newRequestQueue(armada_operasional.this);
                              requestQueue2.add(stringRequest2);

                            }
                        }
                    });

                    StringRequest jenisarmada = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Jenisarmada",
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
                                                String satuan = jsonObject1.getString("satuan");
                                                IdJenisArmada.add(id);
                                                JenisArmada.add(satuan);

                                            }
                                        }
                                        jeniskapasitas.setAdapter(new ArrayAdapter<String>(armada_operasional.this, android.R.layout.simple_expandable_list_item_1, JenisArmada));
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
                    jenisarmada.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue jenisarmadaQueue = Volley.newRequestQueue(armada_operasional.this);
                    jenisarmadaQueue.add(jenisarmada);

                    dialog.show();
                }






            }
        });

        getKendaraan();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(armada_operasional.this);
                dialog.setContentView(R.layout.armada);

                Button batal = dialog.findViewById(R.id.batal);
                Button tambahkan = dialog.findViewById(R.id.tambahkan);

                final AutoCompleteTextView editjenis = dialog.findViewById(R.id.editjenis);
                final AutoCompleteTextView jeniskapasitas = dialog.findViewById(R.id.jeniskapasitas);
                final EditText qty = dialog.findViewById(R.id.qty);



                final ArrayList<String> Armada = new ArrayList<>();

                final ArrayList<String> IdJenisArmada = new ArrayList<>();
                final ArrayList<String> JenisArmada = new ArrayList<>();


                jeniskapasitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        id_JenisArmada = IdJenisArmada.get(position).toString();
                        jeniskapasitas.setError(null);

                    }
                });

                final int[] nomor = new int[1];
                StringRequest armada = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_armada",
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
                                            karyawan = jsonObject1.getString("armada");
                                            Armada.add(karyawan);

                                        }
                                    }
                                    editjenis.setAdapter(new ArrayAdapter<String>(armada_operasional.this, android.R.layout.simple_expandable_list_item_1, Armada));
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
                armada.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue armadaQueue = Volley.newRequestQueue(armada_operasional.this);
                armadaQueue.add(armada);

                StringRequest jenisarmada = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Jenisarmada",
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
                                            String satuan = jsonObject1.getString("satuan");
                                            IdJenisArmada.add(id);
                                            JenisArmada.add(satuan);

                                        }
                                    }
                                    jeniskapasitas.setAdapter(new ArrayAdapter<String>(armada_operasional.this, android.R.layout.simple_expandable_list_item_1, JenisArmada));
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
                jenisarmada.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue jenisarmadaQueue = Volley.newRequestQueue(armada_operasional.this);
                jenisarmadaQueue.add(jenisarmada);

                editjenis.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            editjenis.showDropDown();
                        }
                    }
                });
                editjenis.setFocusable(false);
                editjenis.setLongClickable(false);
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
                        editjenis.setError(null);
                        nomor[0] = position + 1;
                    }
                });

                ImageButton minuskendaraan = dialog.findViewById(R.id.minuskendaraan);
                final EditText editkendaraan = dialog.findViewById(R.id.editkendaraan);
                ImageButton addkendaraan = dialog.findViewById(R.id.addkendaraan);

                final int[] count = new int[1];

                editkendaraan.setText("0");
                addkendaraan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count[0]++;
                        editkendaraan.setText(String.valueOf(count[0]));
                    }

                });
                minuskendaraan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editkendaraan.setText(String.valueOf(count[0]));
                        if (count[0] == 0) {
                            return;
                        }
                        count[0]--;
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
                        if(editjenis.getText().toString().length() == 0){
                            editjenis.setError("Pilih Jenis Kendaraan");
                        } else if (editkendaraan.getText().toString().length() == 0){
                            editkendaraan.setError("Isi Qty");
                        } else if (jeniskapasitas.getText().toString().length() == 0){
                            jeniskapasitas.setError("Isi Jenis Kapasitas");
                        } else if (qty.getText().toString().length() == 0){
                            qty.setError("Isi Qty");
                        } else {
                            final SweetAlertDialog[] pDialog = {new SweetAlertDialog(armada_operasional.this, SweetAlertDialog.PROGRESS_TYPE)};
                            pDialog[0].getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog[0].setCancelable(false);
                            pDialog[0].setTitleText("Harap Menunggu");
                            pDialog[0].show();
                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_armada_kapasitas_satuan",
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            pDialog[0].cancel();
                                            SweetAlertDialog success = new SweetAlertDialog(armada_operasional.this, SweetAlertDialog.SUCCESS_TYPE);
                                            success.setContentText("Berhasil disimpan");
                                            success.setCancelable(false);
                                            success.setConfirmText("OK");
                                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    dialog.dismiss();
                                                    getKendaraan();

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
                                    params.put("armada", String.valueOf(nomor[0]));
                                    params.put("unit", editkendaraan.getText().toString());

                                    params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                                    params.put("longitude", MainActivity.longitude);
                                    params.put("latitude", MainActivity.latitude);

                                    params.put("satuan", id_JenisArmada);
                                    params.put("kapasitas", qty.getText().toString());
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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(armada_operasional.this);
                            requestQueue2.add(stringRequest2);
                        }
                    }
                });

                dialog.show();
            }
        });
    }

    private void getKendaraan() {
        armada_pojoList.clear();
        final SweetAlertDialog pDialog = new SweetAlertDialog(armada_operasional.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_kendaraan?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final armada_pojo movieItem = new armada_pojo(
                                        movieObject.getString("id"),
                                        movieObject.getString("armada"),
                                        movieObject.getString("unit"),
                                        movieObject.getString("satuan"),
                                        movieObject.getString("kapasitas"));
                                armada_pojoList.add(movieItem);

                            }
                            adapter = new ListViewAdapterKendaraan(armada_pojoList, getApplicationContext());
                            armada.setAdapter(adapter);
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
    public static class ListViewAdapterKendaraan extends ArrayAdapter<armada_pojo> {

        List<armada_pojo> armada_pojos;

        private Context context;

        public ListViewAdapterKendaraan(List<armada_pojo> armada_pojos, Context context) {
            super(context, R.layout.list_item_armada, armada_pojos);
            this.armada_pojos = armada_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_armada, null, true);

            TextView namaproduk = listViewItem.findViewById(R.id.namaproduk);
            TextView qty = listViewItem.findViewById(R.id.qty);
            TextView kapasitas = listViewItem.findViewById(R.id.kapasitas);
            ImageView ubah = listViewItem.findViewById(R.id.ubah);

            armada_pojo armada_pojo = getItem(position);

            namaproduk.setText(armada_pojo.getArmada());
            qty.setText(armada_pojo.getUnit());

            if(armada_pojo.getKapasitas().equals("0")){
                ubah.setVisibility(View.VISIBLE);
                kapasitas.setText("Tidak Ada Kapasitas");
            } else {
                kapasitas.setText(armada_pojo.getKapasitas() + " " + armada_pojo.getSatuan());
            }

            return listViewItem;
        }
    }
}