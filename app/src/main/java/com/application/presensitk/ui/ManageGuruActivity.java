package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityManageGuruBinding;
import com.application.presensitk.model.guru.GuruModel;
import com.application.presensitk.model.guru.GuruResponseModel;
import com.application.presensitk.service.ApiService;
import com.application.presensitk.utils.adapter.ListGuruRVAdapter;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManageGuruActivity extends AppCompatActivity {

    ActivityManageGuruBinding binding;
    SPHelper spHelper;
    ListGuruRVAdapter adapter;
    ArrayList<GuruModel> datas;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageGuruBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        datas = new ArrayList<>();
        binding.rvGuru.setHasFixedSize(true);
        binding.rvGuru.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        readData();
        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageGuruActivity.this, InputGuruActivity.class);
                startActivity(intent);
            }
        });
        binding.fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });
    }

    void readData(){
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
        service.readGuru().enqueue(new Callback<GuruResponseModel>() {
            @Override
            public void onResponse(Call<GuruResponseModel> call, Response<GuruResponseModel> response) {
                if(response.isSuccessful()){
                    GuruResponseModel guruResponseModel = response.body();
                    datas = guruResponseModel.getResult();
                    adapter = new ListGuruRVAdapter(ManageGuruActivity.this, datas);
                    adapter.notifyDataSetChanged();
                    binding.rvGuru.setAdapter(adapter);

                    Toast.makeText(ManageGuruActivity.this, "" + guruResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GuruResponseModel> call, Throwable t) {
                Log.d("Error Read", "" + t.getMessage());
            }
        });
    }

    public void deleteData(Integer id_guru){
        progressDialog.setMessage("Menghapus...");
        progressDialog.setCancelable(true);
        progressDialog.show();

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
        service.deleteGuru(id_guru).enqueue(new Callback<GuruResponseModel>() {
            @Override
            public void onResponse(Call<GuruResponseModel> call, Response<GuruResponseModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus()){
                        readData();
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GuruResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("Error Delete", "" + t.getMessage());
            }
        });
    }
}