package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.util.Calendar.DAY_OF_MONTH;

public class profile_outlet extends AppCompatActivity {
    Button batal, selanjutnya;

    SharedPreferences sharedPreferences;

    static EditText editalamatoutlet;
    static EditText editRToutlet;
    static EditText editRWoutlet;
    static EditText editKodePosoutlet, editberlangganan;
    EditText editteleponoutlet;
    static EditText editjaraktempuh;
    static EditText editkaryawan, edittanggallahir;
    static CheckBox check1, check2, check3, check4, check5, check6;

    static RadioGroup buttonparkir;

    static CheckBox lokasi1, lokasi2, lokasi3, lokasi4, lokasi5;
    static RadioButton parkir1, parkir2, parkir3, parkir4;
    static CheckBox order1, order2, order3, order4;
    static CheckBox chiller1, chiller2, chiller3, chiller4, chiller5;

    static CheckBox ya;
    CheckBox tidak;

    Calendar tanggalfullday;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy",Locale.getDefault());

    public void tanggal() {
        final Calendar currentDate = Calendar.getInstance();
        tanggalfullday = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                tanggalfullday.set(year, monthOfYear, dayOfMonth);

                editberlangganan.setText(dateFormatter.format(tanggalfullday.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(profile_outlet.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void tanggallahir() {
        final Calendar currentDate = Calendar.getInstance();
        tanggalfullday = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                tanggalfullday.set(year, monthOfYear, dayOfMonth);

                edittanggallahir.setText(dateFormatter.format(tanggalfullday.getTime()));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(profile_outlet.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    static AutoCompleteTextView editkotaoutlet;
    static AutoCompleteTextView editkecamatanoutlet;
    static AutoCompleteTextView editkelurahanoutlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_outlet);
        HttpsTrustManager.allowAllSSL();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szId", null);
        String rest = nik_baru;
        String[] parts = rest.split("-");
        String restnomor = parts[0];
        String restnomorbaru = restnomor.replace(" ", "");

        batal = findViewById(R.id.batal);
        selanjutnya = findViewById(R.id.selanjutnya);

        buttonparkir = findViewById(R.id.buttonparkir);

        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        check3 = findViewById(R.id.check3);
        check4 = findViewById(R.id.check4);
        check5 = findViewById(R.id.check5);
        check6 = findViewById(R.id.check6);
        edittanggallahir = findViewById(R.id.edittanggallahir);

        lokasi1 = findViewById(R.id.lokasi1);
        lokasi2 = findViewById(R.id.lokasi2);
        lokasi3 = findViewById(R.id.lokasi3);
        lokasi4 = findViewById(R.id.lokasi4);
        lokasi5 = findViewById(R.id.lokasi5);

        parkir1 = findViewById(R.id.parkir1);
        parkir2 = findViewById(R.id.parkir2);
        parkir3 = findViewById(R.id.parkir3);
        parkir4 = findViewById(R.id.parkir4);

        order1 = findViewById(R.id.order1);
        order2 = findViewById(R.id.order2);
        order3 = findViewById(R.id.order3);
        order4 = findViewById(R.id.order4);

        chiller1 = findViewById(R.id.chiller1);
        chiller2 = findViewById(R.id.chiller2);
        chiller3 = findViewById(R.id.chiller3);
        chiller4 = findViewById(R.id.chiller4);
        chiller5 = findViewById(R.id.chiller5);


        editalamatoutlet = findViewById(R.id.editalamatoutlet);
        editRToutlet = findViewById(R.id.editRToutlet);
        editRWoutlet = findViewById(R.id.editRWoutlet);
        editteleponoutlet = findViewById(R.id.editteleponoutlet);
        editberlangganan = findViewById(R.id.editberlangganan);

        editjaraktempuh = findViewById(R.id.editjaraktempuh);
        editkaryawan = findViewById(R.id.editkaryawan);
        editKodePosoutlet = findViewById(R.id.editKodePosoutlet);

        editkotaoutlet = findViewById(R.id.editkotaoutlet);
        editkecamatanoutlet = findViewById(R.id.editkecamatanoutlet);
        editkelurahanoutlet = findViewById(R.id.editkelurahanoutlet);
        editKodePosoutlet = findViewById(R.id.editKodePosoutlet);

        ya = findViewById(R.id.ya);
        tidak = findViewById(R.id.tidak);

        edittanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanggallahir();
            }
        });

        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check2.setChecked(false);
                    check3.setChecked(false);
                    check4.setChecked(false);
                }
            }
        });

        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check1.setChecked(false);
                    check3.setChecked(false);
                    check4.setChecked(false);
                }
            }
        });

        check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check1.setChecked(false);
                    check2.setChecked(false);
                    check4.setChecked(false);
                }
            }
        });

        check4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check1.setChecked(false);
                    check2.setChecked(false);
                    check3.setChecked(false);
                }
            }
        });





        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_login?szId=" + nik_baru + "&kode_dms=" + restnomorbaru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {

                                    final JSONObject movieObject = movieArray.getJSONObject(i);

                                    editalamatoutlet.setText(movieObject.getString("szAddress"));
                                    editkotaoutlet.setText(movieObject.getString("szCity"));
                                    editkecamatanoutlet.setText(movieObject.getString("szDistrict"));
                                    editkelurahanoutlet.setText(movieObject.getString("szSubDistrict"));
                                    editKodePosoutlet.setText(movieObject.getString("szZipCode"));
                                    editteleponoutlet.setText(movieObject.getString("szPhone1"));
                                    editalamatoutlet.setText(movieObject.getString("szAddress"));

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
        RequestQueue requestQueue = Volley.newRequestQueue(profile_outlet.this);
        requestQueue.add(stringRequest);


        editberlangganan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tanggal();
            }
        });


        ya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tidak.setChecked(false);
                }
            }
        });

        tidak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ya.setChecked(false);
                }
            }
        });

        editkotaoutlet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editkotaoutlet.showDropDown();
            }
        });
        editkotaoutlet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editkotaoutlet.showDropDown();
                return false;
            }
        });

        editkelurahanoutlet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editkelurahanoutlet.showDropDown();
            }
        });
        editkelurahanoutlet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editkelurahanoutlet.showDropDown();
                return false;
            }
        });

        editkecamatanoutlet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editkecamatanoutlet.showDropDown();
            }
        });
        editkecamatanoutlet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editkecamatanoutlet.showDropDown();
                return false;
            }
        });


        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editteleponoutlet.getText().toString().length() == 0){
                    editteleponoutlet.setError("Isi Telepon");
                } else if (editjaraktempuh.getText().toString().length() == 0){
                    editjaraktempuh.setError("Isi KM");
                } else if (editkaryawan.getText().toString().length() == 0){
                    editkaryawan.setError("Isi Jumlah Karyawan");
                } else if (editberlangganan.getText().toString().length() == 0){
                    editberlangganan.setError("Isi Tanggal Mulai Berlangganan");
                } else if (!check1.isChecked() && !check2.isChecked() && !check3.isChecked() &&
                        !check4.isChecked() && !check5.isChecked() && !check6.isChecked()){
                    new SweetAlertDialog(profile_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kategori Outlet")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (!ya.isChecked() && !tidak.isChecked()){
                    new SweetAlertDialog(profile_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih ya atau tidak")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }  else if (!lokasi1.isChecked() && !lokasi2.isChecked() && !lokasi3.isChecked() && !lokasi4.isChecked() && !lokasi5.isChecked()){
                new SweetAlertDialog(profile_outlet.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Pilih Lokasi")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else if (buttonparkir.getCheckedRadioButtonId() == -1){
                new SweetAlertDialog(profile_outlet.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Pilih Tempat Parkir")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else if (!order1.isChecked() && !order2.isChecked() && !order3.isChecked() && !order4.isChecked()){
                new SweetAlertDialog(profile_outlet.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Pilih Cara Order")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else if (!chiller1.isChecked() && !chiller2.isChecked() && !chiller3.isChecked() && !chiller4.isChecked() && !chiller5.isChecked())

            {
                new SweetAlertDialog(profile_outlet.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Pilih Pendingin")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else {
                    Intent intent = new Intent(getBaseContext(), gudang.class);
                    startActivity(intent);
                }


            }
        });

    }
}