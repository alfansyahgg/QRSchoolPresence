package com.application.presensitk.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.application.presensitk.config.SPHelper;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityLoginBinding;
import com.application.presensitk.model.auth.LoginModel;
import com.application.presensitk.model.auth.ResponseModel;
import com.application.presensitk.service.ApiService;

import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private String API_URL;
    ActivityLoginBinding binding;
    SPHelper spHelper;
    int RC_CAMERA_AND_LOCATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spHelper = new SPHelper(this);

        methodRequiresTwoPermission();

        if(spHelper.readValue("login", false)){
            if(spHelper.isAdmin()){
                Intent intent = new Intent(LoginActivity.this, HomeAdminActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(LoginActivity.this, HomeGuruActivity.class);
                startActivity(intent);
            }
            finish();
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString().trim().toLowerCase(Locale.ROOT);
                String passsword = binding.etPassword.getText().toString().trim();

                if(!binding.rbGuru.isChecked() && !binding.rbAdmin.isChecked()){
                    Toast.makeText(LoginActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (binding.rbGuru.isChecked()){
                    API_URL = StaticStuffs.BASE_URL_GURU;
                }else{
                    API_URL = StaticStuffs.BASE_URL_ADMIN;
                }

                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.level(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();

                ApiService service = retrofit.create(ApiService.class);
                service.login(email, passsword).enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        if(response.isSuccessful()){
                            LoginModel data = response.body();
                            Toast.makeText(LoginActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            if(data.getStatus()){
                                spHelper.saveValue("login", true);
                                ResponseModel respon = data.getData();
                                spHelper.saveValue("id_admin", respon.getId_admin());
                                spHelper.saveValue("id_guru", respon.getId_guru());
                                spHelper.saveValue("nama", respon.getNama());
                                spHelper.saveValue("nohp", respon.getNohp());
                                spHelper.saveValue("email", respon.getEmail());
                                spHelper.saveValue("password", respon.getPassword());
                                if (data.getAdmin()){
                                    spHelper.saveValue("admin", true);
                                    Intent intent = new Intent(LoginActivity.this, HomeAdminActivity.class);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(LoginActivity.this, HomeGuruActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            }
                            Log.d("Cek Respon", "" + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        Log.d("Cek Error", "" + t.getMessage());
                    }
                });
            }
        });
    }

    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Izinkan Aplikasi Untuk Menggunakan Kamera dan Akses Lokasi", RC_CAMERA_AND_LOCATION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == RC_CAMERA_AND_LOCATION){
            Toast.makeText(this, "Diizinkan!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(requestCode == RC_CAMERA_AND_LOCATION){
            EasyPermissions.requestPermissions(LoginActivity.this, "Izinkan Aplikasi Untuk Menggunakan Kamera dan Akses Lokasi", RC_CAMERA_AND_LOCATION, String.valueOf(perms));
        }
    }

}