package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.tvip.surveylance.pojo.ratiokosongpojo;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class list_galon_kosong extends AppCompatActivity {
    MaterialToolbar tahunberjalan;
    ImageView filtering;
    List<ratiokosongpojo> ratiokosongpojoList = new ArrayList<>();
    String tahun;
    SharedPreferences sharedPreferences;
    ListViewAdapterGalonKosong adapter;
    ListView list_kosong;
    RelativeLayout peringatan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_galon_kosong);
        HttpsTrustManager.allowAllSSL();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String currentDateandTime = sdf.format(new Date());
        tahun = currentDateandTime;

        list_kosong = findViewById(R.id.list_kosong);
        peringatan = findViewById(R.id.peringatan);

        tahunberjalan = findViewById(R.id.tahunberjalan);
        filtering = findViewById(R.id.filtering);

        tahunberjalan.setTitle("Survei Rasio Galon " + currentDateandTime);

        list_kosong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ID = ((ratiokosongpojo) parent.getItemAtPosition(position)).getId_ratio();
                String depo = ((ratiokosongpojo) parent.getItemAtPosition(position)).getDepo();
                String segment = ((ratiokosongpojo) parent.getItemAtPosition(position)).getSegment();
                String total = ((ratiokosongpojo) parent.getItemAtPosition(position)).getTotal();

                String rata = ((ratiokosongpojo) parent.getItemAtPosition(position)).getRata_rata();
                String standar = ((ratiokosongpojo) parent.getItemAtPosition(position)).getStandar();
                String luas = ((ratiokosongpojo) parent.getItemAtPosition(position)).getLuas_gudang();
                String stock_kosong = ((ratiokosongpojo) parent.getItemAtPosition(position)).getJumlah_stok();
                String dtmSurvey = ((ratiokosongpojo) parent.getItemAtPosition(position)).getDtmSurvey();
                String bulan = ((ratiokosongpojo) parent.getItemAtPosition(position)).getBulan();
                String tahun = ((ratiokosongpojo) parent.getItemAtPosition(position)).getTahun();

                String tglupdate = ((ratiokosongpojo) parent.getItemAtPosition(position)).getDtmLastUpdate();


                String bulantahun = convertFormat(bulan+"-"+tahun);

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
                String currentDateandTime = sdf.format(new Date());

                String sekarang = currentDateandTime;
                String tahundepan = bulantahun;

                Date date1 = null;
                try {
                    date1 = sdf.parse(sekarang);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = null;
                try {
                    date2 = sdf.parse(tahundepan);
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                if (date1.before(date2)) {
                    if(luas.equals("null")){
                        new SweetAlertDialog(list_galon_kosong.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Maaf Bulan Ini Belum Bisa Di Survey")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                    } else {
                        Intent i = new Intent(list_galon_kosong.this, detail_galon_kosong.class);

                        i.putExtra("ID", ID);
                        i.putExtra("segment", segment);
                        i.putExtra("total", total);
                        i.putExtra("depo", depo);
                        i.putExtra("dtmSurvey", dtmSurvey);
                        i.putExtra("rata", rata);
                        i.putExtra("standar", standar);
                        i.putExtra("bulantahun", bulantahun);
                        i.putExtra("luas", luas);
                        i.putExtra("stock_kosong", stock_kosong);
                        i.putExtra("tglupdate", tglupdate);

                        startActivity(i);
                    }
                } else if (date1.after(date2)){
                    if(luas.equals("null")){
                        new SweetAlertDialog(list_galon_kosong.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Maaf Bulan Ini Sudah Tidak Bisa Di Survey")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                    } else {
                        Intent i = new Intent(list_galon_kosong.this, detail_galon_kosong.class);

                        i.putExtra("ID", ID);
                        i.putExtra("segment", segment);
                        i.putExtra("total", total);
                        i.putExtra("depo", depo);
                        i.putExtra("dtmSurvey", dtmSurvey);
                        i.putExtra("rata", rata);
                        i.putExtra("standar", standar);
                        i.putExtra("bulantahun", bulantahun);
                        i.putExtra("luas", luas);
                        i.putExtra("stock_kosong", stock_kosong);
                        i.putExtra("tglupdate", tglupdate);
                        startActivity(i);
                    }
                } else if (date1.equals(date2)){
                    Intent i = new Intent(list_galon_kosong.this, detail_galon_kosong.class);

                    i.putExtra("ID", ID);
                    i.putExtra("segment", segment);
                    i.putExtra("total", total);
                    i.putExtra("depo", depo);
                    i.putExtra("dtmSurvey", dtmSurvey);

                    i.putExtra("rata", rata);
                    i.putExtra("standar", standar);
                    i.putExtra("bulantahun", bulantahun);
                    i.putExtra("luas", luas);
                    i.putExtra("stock_kosong", stock_kosong);
                    i.putExtra("tglupdate", tglupdate);
                    startActivity(i);
                }
            }
        });

        filtering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(list_galon_kosong.this);
                dialog.setContentView(R.layout.pilih_tanggal);
                dialog.show();


                Button batal = dialog.findViewById(R.id.batal);
                Button ok = dialog.findViewById(R.id.ok);

                final EditText edittahun = dialog.findViewById(R.id.edittahun);

                edittahun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar tahun = Calendar.getInstance();
                        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(list_galon_kosong.this, new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                edittahun.setText(String.valueOf(selectedYear));
                            }
                        }, tahun.get(Calendar.YEAR), tahun.get(Calendar.MONTH));

                        builder.setActivatedMonth(Calendar.JULY)
                                .setMinYear(1990)
                                .setActivatedYear(tahun.get(Calendar.YEAR))
                                .setMaxYear(2999)
                                .setTitle("Pilih Tahun")
                                .showYearOnly()
                                .build().show();

//
                    }
                });

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edittahun.getText().toString().length() == 0){
                            edittahun.setError("Isi Tahun");
                        } else {
                            dialog.dismiss();
                            tahun = edittahun.getText().toString();
                            getDataKosong();
                            tahunberjalan.setTitle("Survei Rasio Galon " + tahun);
                        }
                    }
                });
            }
        });


    }

    private void getDataKosong() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(list_galon_kosong.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        ratiokosongpojoList.clear();

        adapter = new ListViewAdapterGalonKosong(ratiokosongpojoList, getApplicationContext());
        list_kosong.setAdapter(adapter);


        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/ratio_galon_kosong/ratio/index?id="+szId+"&tahun="+tahun ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        list_kosong.setVisibility(View.VISIBLE);
                        peringatan.setVisibility(View.GONE);

                        try {
                            pDialog.dismissWithAnimation();
                            JSONObject obj = new JSONObject(response);
                            JSONArray movieArray = obj.getJSONArray("data");

                            for (int i = 0; i < movieArray.length(); i++) {

                                JSONObject movieObject = movieArray.getJSONObject(i);

                                ratiokosongpojo movieItem = new ratiokosongpojo(
                                        movieObject.getString("id_ratio"),
                                        movieObject.getString("depo"),
                                        movieObject.getString("segment"),
                                        movieObject.getString("bulan"),
                                        movieObject.getString("tahun"),
                                        movieObject.getString("total"),
                                        movieObject.getString("rata_rata"),
                                        movieObject.getString("standar"),
                                        movieObject.getString("jumlah_stok"),
                                        movieObject.getString("luas_gudang"),
                                        movieObject.getString("user_input"),
                                        movieObject.getString("nik_survey"),
                                        movieObject.getString("dtmSurvey"),
                                        movieObject.getString("dtmLastUpdate"),
                                        movieObject.getString("dtmCreate"),
                                        movieObject.getString("submit_date"));

                                if (!ratiokosongpojoList.contains(movieItem)) {
                                    ratiokosongpojoList.add(movieItem);
                                }
                                adapter.notifyDataSetChanged();

                                Collections.sort(ratiokosongpojoList, new Comparator<ratiokosongpojo>() {
                                    public int compare(ratiokosongpojo o2, ratiokosongpojo o1) {
                                        if (o2.getBulan() == null || o1.getBulan() == null)
                                            return 0;
                                        return o2.getBulan().compareTo(o1.getBulan());
                                    }
                                });


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        list_kosong.setAdapter(null);
                        list_kosong.setVisibility(View.GONE);
                        peringatan.setVisibility(View.VISIBLE);
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
                )
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static class ListViewAdapterGalonKosong extends ArrayAdapter<ratiokosongpojo> {

        List<ratiokosongpojo> ratiokosongpojos;

        private Context context;

        public ListViewAdapterGalonKosong(List<ratiokosongpojo> ratiokosongpojos, Context context) {
            super(context, R.layout.list_item_galon_kosong, ratiokosongpojos);
            this.ratiokosongpojos = ratiokosongpojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_galon_kosong, null, true);


            TextView tanggal = listViewItem.findViewById(R.id.tanggal);
            Chip kosong = listViewItem.findViewById(R.id.kosong);
            Chip ideal = listViewItem.findViewById(R.id.ideal);
            Chip tidak_ideal = listViewItem.findViewById(R.id.tidak_ideal);

            ratiokosongpojo ratiokosongpojo = getItem(position);

            tanggal.setText(convertFormat(ratiokosongpojo.getBulan()+"-"+ratiokosongpojo.getTahun()));



            if(ratiokosongpojo.getLuas_gudang().equals("null")){
                kosong.setVisibility(View.VISIBLE);
            } else {
                int total = Integer.parseInt(ratiokosongpojo.getLuas_gudang()) * 48;
                if(total >= Integer.parseInt(ratiokosongpojo.getJumlah_stok())){
                    ideal.setVisibility(View.VISIBLE);
                } else if(total < Integer.parseInt(ratiokosongpojo.getJumlah_stok())){
                    tidak_ideal.setVisibility(View.VISIBLE);
                }
            }




            return listViewItem;
        }
    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
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

    @Override
    protected void onResume() {
        super.onResume();
        getDataKosong();
    }
}