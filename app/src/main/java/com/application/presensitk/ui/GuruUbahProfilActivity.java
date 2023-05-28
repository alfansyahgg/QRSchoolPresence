package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityGuruUbahProfilBinding;
import com.application.presensitk.model.guru.GuruResponseModel;
import com.application.presensitk.service.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GuruUbahProfilActivity extends AppCompatActivity {

    ActivityGuruUbahProfilBinding binding;
    SPHelper spHelper;
    Integer id_guru, id_admin;
    String nama, nohp, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuruUbahProfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spHelper = new SPHelper(this);
        id_admin = spHelper.readValue("id_admin", 0);
        id_guru = spHelper.readValue("id_guru", 0);
        nama = spHelper.readValue("nama", "");
        nohp = spHelper.readValue("nohp", "");
        email = spHelper.readValue("email", "");
        password = spHelper.readValue("password", "");

        binding.etNama.setText(nama);
        binding.etNohp.setText(nohp);
        binding.etEmail.setText(email);

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

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.etNama.getText().toString().equals("") && !binding.etNohp.getText().toString().equals("") && !binding.etEmail.getText().toString().equals("") && !binding.etPassword.getText().toString().equals("")) {
                    String nama = binding.etNama.getText().toString();
                    String nohp = binding.etNohp.getText().toString();
                    String email = binding.etEmail.getText().toString();
                    String password = binding.etPassword.getText().toString();
                    String tipe;
                    if(id_guru == 0){
                        id_guru = id_admin;
                        tipe = "admin";
                    }else{
                        tipe = "guru";
                    }
                    service.ubahProfil(
                            id_guru,
                            nama,
                            nohp,
                            email,
                            password,
                            tipe
                    ).enqueue(new Callback<GuruResponseModel>() {
                        @Override
                        public void onResponse(Call<GuruResponseModel> call, Response<GuruResponseModel> response) {
                            if(response.isSuccessful()){
                                GuruResponseModel guruResponseModel = response.body();
                                Toast.makeText(GuruUbahProfilActivity.this, "" + guruResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(GuruUbahProfilActivity.this, LoginActivity.class);
                                spHelper.saveValue("login", false);
                                spHelper.saveValue("admin", false);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finishAffinity();
                            }
                        }

                        @Override
                        public void onFailure(Call<GuruResponseModel> call, Throwable t) {
                            Log.d("Error Create", "" + t.getMessage());
                        }
                    });
                }
            }
        });
    }
}