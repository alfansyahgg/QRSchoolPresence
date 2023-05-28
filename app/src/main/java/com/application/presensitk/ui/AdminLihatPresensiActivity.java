package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityAdminLihatPresensiBinding;
import com.application.presensitk.model.presensi.AdminPresensiModel;
import com.application.presensitk.model.presensi.DetailAdminPresensiModel;
import com.application.presensitk.model.presensi.PresensiModel;
import com.application.presensitk.service.ApiService;
import com.application.presensitk.utils.adapter.ListAdminPresensiRVAdapter;
import com.application.presensitk.utils.adapter.ListPresensiRVAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminLihatPresensiActivity extends AppCompatActivity {

    ActivityAdminLihatPresensiBinding binding;
    SPHelper spHelper;
    String jenis, tanggal;
    ListAdminPresensiRVAdapter adapter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLihatPresensiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spHelper = new SPHelper(this);
        Intent intent = getIntent();
        if(intent != null){
            jenis = intent.getStringExtra("jenis");
        }
        String date;
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tanggal = "";
        binding.tvSelected.setText("Tanggal : " + date);
        binding.rvPresensi.setHasFixedSize(true);
        binding.btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminLihatPresensiActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                c.set(Calendar.HOUR_OF_DAY, mHour);
                                c.set(Calendar.MINUTE, mMinute);
                                c.set(Calendar.SECOND, mMinute = c.get(Calendar.SECOND));
                                tanggal = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                binding.tvSelected.setText("Tanggal : " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                binding.tvInfo.setText("Hasil Pencarian : " + tanggal);
                                getData(tanggal);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        getData(tanggal);
        binding.fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(tanggal);
            }
        });
    }

    void getData(String tanggal){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticStuffs.BASE_URL_ADMIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        service.adminReadPresensi(jenis, tanggal).enqueue(new Callback<AdminPresensiModel>() {
            @Override
            public void onResponse(Call<AdminPresensiModel> call, Response<AdminPresensiModel> response) {
                if(response.isSuccessful()){
                    AdminPresensiModel adminPresensiModel = response.body();
                    Toast.makeText(AdminLihatPresensiActivity.this, "" + adminPresensiModel.getMessage(), Toast.LENGTH_SHORT).show();
                    adapter = new ListAdminPresensiRVAdapter(AdminLihatPresensiActivity.this, adminPresensiModel.getData());
                    binding.rvPresensi.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<AdminPresensiModel> call, Throwable t) {
                Toast.makeText(AdminLihatPresensiActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}