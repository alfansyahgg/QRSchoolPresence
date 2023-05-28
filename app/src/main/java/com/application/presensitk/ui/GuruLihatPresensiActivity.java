package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityGuruLihatPresensiBinding;
import com.application.presensitk.model.presensi.PresensiModel;
import com.application.presensitk.service.ApiService;
import com.application.presensitk.utils.adapter.ListPresensiRVAdapter;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GuruLihatPresensiActivity extends AppCompatActivity {

    ActivityGuruLihatPresensiBinding binding;
    SPHelper spHelper;
    String jenis;
    ListPresensiRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuruLihatPresensiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spHelper = new SPHelper(this);

        Intent intent = getIntent();
        if(intent != null){
            jenis = intent.getStringExtra("jenis");
        }

        binding.rvPresensi.setHasFixedSize(true);

        getData();

        binding.fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    void getData(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticStuffs.BASE_URL_GURU)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ApiService service = retrofit.create(ApiService.class);
        service.readPresensi(jenis, spHelper.readValue("id_guru", 0)).enqueue(new Callback<PresensiModel>() {
            @Override
            public void onResponse(Call<PresensiModel> call, Response<PresensiModel> response) {
                if(response.isSuccessful()){
                    PresensiModel presensiModel = response.body();
                    Toast.makeText(GuruLihatPresensiActivity.this, "" + presensiModel.getMessage(), Toast.LENGTH_SHORT).show();
                    if(presensiModel.getData() != null){
                        if(presensiModel.getData().size() > 0){
                            adapter = new ListPresensiRVAdapter(GuruLihatPresensiActivity.this, presensiModel.getData());
                            binding.rvPresensi.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<PresensiModel> call, Throwable t) {
                Toast.makeText(GuruLihatPresensiActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}