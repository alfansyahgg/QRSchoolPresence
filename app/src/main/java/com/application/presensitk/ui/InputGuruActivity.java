package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityInputGuruBinding;
import com.application.presensitk.model.guru.GuruModel;
import com.application.presensitk.model.guru.GuruResponseModel;
import com.application.presensitk.service.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InputGuruActivity extends AppCompatActivity {

    ActivityInputGuruBinding binding;
    String jenis;
    Integer id_guru = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInputGuruBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if(intent != null){
            jenis = "edit";
            id_guru = intent.getIntExtra("id_guru", 0);
            String nama = intent.getStringExtra("nama");
            String nohp = intent.getStringExtra("nohp");
            String email = intent.getStringExtra("email");
            String password = intent.getStringExtra("password");

            binding.etNama.setText(nama);
            binding.etNohp.setText(nohp);
            binding.etEmail.setText(email);
        }

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

        binding.btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama, nohp, email, password;
                if(!binding.etNama.getText().toString().equals("") && !binding.etNohp.getText().toString().equals("") && !binding.etEmail.getText().toString().equals("") && !binding.etPassword.getText().toString().equals("")){
                    nama = binding.etNama.getText().toString().trim();
                    nohp = binding.etNohp.getText().toString().trim();
                    email = binding.etEmail.getText().toString().trim();
                    password = binding.etPassword.getText().toString().trim();

                    if(jenis.equals("edit") && id_guru != 0){
                        service.updateGuru(id_guru, nama, nohp, email, password).enqueue(new Callback<GuruResponseModel>() {
                            @Override
                            public void onResponse(Call<GuruResponseModel> call, Response<GuruResponseModel> response) {
                                if(response.isSuccessful()){
                                    GuruResponseModel guruResponseModel = response.body();
                                    Toast.makeText(InputGuruActivity.this, "" + guruResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    if(guruResponseModel.getStatus()){
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GuruResponseModel> call, Throwable t) {
                                Log.d("Error Update", "" + t.getMessage());
                            }
                        });
                    }else{
                        service.createGuru(nama, nohp, email, password).enqueue(new Callback<GuruResponseModel>() {
                            @Override
                            public void onResponse(Call<GuruResponseModel> call, Response<GuruResponseModel> response) {
                                if(response.isSuccessful()){
                                    GuruResponseModel guruResponseModel = response.body();
                                    Toast.makeText(InputGuruActivity.this, "" + guruResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    if(guruResponseModel.getStatus()){
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GuruResponseModel> call, Throwable t) {
                                Log.d("Error Create", "" + t.getMessage());
                            }
                        });
                    }

                }else{
                    Toast.makeText(InputGuruActivity.this, "Invalid : Mohon Isi Data!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}