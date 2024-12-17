package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.tvip.surveylance.pojo.armada_pojo;
import com.tvip.surveylance.pojo.draftkompetitor_pojo;
import com.tvip.surveylance.pojo.ratiokosongpojo;

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

public class list_survey_kompetitor extends AppCompatActivity {
    ListView list_date;
    TextView tanggaltext;
    ImageView filtering;
    RelativeLayout peringatan;

    String mDateStart, mDateEnd;
    List<draftkompetitor_pojo> draftkompetitorPojoDateList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SweetAlertDialog pDialog;

    ListViewAdapterKompetitorByDate adapterKompetitorByDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_survey_kompetitor);
        list_date = findViewById(R.id.list_date);
        tanggaltext = findViewById(R.id.tanggaltext);
        filtering = findViewById(R.id.filtering);
        peringatan = findViewById(R.id.peringatan);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String currentDateandTime = sdf.format(new Date());
        mDateStart = currentDateandTime;
        mDateEnd = currentDateandTime;

        if(mDateStart.equals(mDateEnd)){
            tanggaltext.setText(mDateStart);
        } else {
            tanggaltext.setText(mDateStart + " - " + mDateEnd);
        }



        list_date.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String submit_date = ((draftkompetitor_pojo) parent.getItemAtPosition(position)).getSubmit_date();

                Intent intent = new Intent(getApplicationContext(), list_all_details.class);
                intent.putExtra("submit_date", submit_date);
                startActivity(intent);
            }
        });

        filtering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(list_survey_kompetitor.this);
                dialog.setContentView(R.layout.pilih_range_tanggal);
                dialog.show();

                Button batal = dialog.findViewById(R.id.batal);
                Button ok = dialog.findViewById(R.id.ok);

                final EditText edittanggal = dialog.findViewById(R.id.edittanggal);
                final EditText editsampaitanggal = dialog.findViewById(R.id.editsampaitanggal);

                MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().build();

                edittanggal.setOnClickListener(new View.OnClickListener() {
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
                                tanggaltext.setText(simpleFormat.format(startDate) + " - " + simpleFormat.format(endDate));
                                edittanggal.setText(simpleFormat.format(startDate));
                                editsampaitanggal.setText(simpleFormat.format(endDate));
                                mDateStart = simpleFormat.format(startDate);
                                mDateEnd = simpleFormat.format(endDate);
                            }
                        });
                    }
                });

                editsampaitanggal.setOnClickListener(new View.OnClickListener() {
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
                                tanggaltext.setText(simpleFormat.format(startDate) + " - " + simpleFormat.format(endDate));
                                edittanggal.setText(simpleFormat.format(startDate));
                                editsampaitanggal.setText(simpleFormat.format(endDate));

                                mDateStart = simpleFormat.format(startDate);
                                mDateEnd = simpleFormat.format(endDate);
                            }
                        });
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
                        if(edittanggal.getText().toString().length() == 0){
                            edittanggal.setError("Isi Tanggal");
                        } else {
                            dialog.dismiss();
                            getDataByDate();
                        }
                    }
                });
            }
        });

    }

    private void getDataByDate() {
        pDialog = new SweetAlertDialog(list_survey_kompetitor.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();

        if (mDateEnd == null) {
            mDateEnd = mDateStart;
        }

        draftkompetitorPojoDateList.clear();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_range?kode_pelanggan="+szId+"&tanggal=" + convertFormat(mDateStart) +"&tanggal2=" + convertFormat(mDateEnd),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                peringatan.setVisibility(View.GONE);
                                list_date.setVisibility(View.VISIBLE);
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final draftkompetitor_pojo movieItem = new draftkompetitor_pojo(
                                        movieObject.getString("id"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("nik_survey"),
                                        movieObject.getString("kode_pelanggan"),
                                        movieObject.getString("nama_jenis"),
                                        movieObject.getString("nama_kiriman"),
                                        movieObject.getString("produk_lain"),
                                        movieObject.getString("brand"),
                                        movieObject.getString("uom_brand"),
                                        movieObject.getString("volume_brand"),
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
                                draftkompetitorPojoDateList.add(movieItem);

                            }

                            adapterKompetitorByDate = new ListViewAdapterKompetitorByDate(draftkompetitorPojoDateList, getApplicationContext());
                            list_date.setAdapter(adapterKompetitorByDate);
                            adapterKompetitorByDate.notifyDataSetChanged();

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
                        list_date.setVisibility(View.GONE);
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

    public static class ListViewAdapterKompetitorByDate extends ArrayAdapter<draftkompetitor_pojo> {

        List<draftkompetitor_pojo> draftkompetitor_pojos;

        private Context context;

        public ListViewAdapterKompetitorByDate(List<draftkompetitor_pojo> draftkompetitor_pojos, Context context) {
            super(context, R.layout.list_item_date, draftkompetitor_pojos);
            this.draftkompetitor_pojos = draftkompetitor_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_date, null, true);


            TextView tanggal = listViewItem.findViewById(R.id.tanggal);

            draftkompetitor_pojo draftkompetitor_pojo = getItem(position);

            tanggal.setText(dateTime(draftkompetitor_pojo.getSubmit_date()));

            return listViewItem;
        }
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

    public static String dateTime(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataByDate();
    }
}