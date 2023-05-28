package com.application.presensitk.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityScanPresensiHadirBinding;
import com.application.presensitk.databinding.ActivityScanPresensiPulangBinding;
import com.application.presensitk.model.presensi.PresensiModel;
import com.application.presensitk.service.ApiService;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScanPresensiPulangActivity extends AppCompatActivity {
    ActivityScanPresensiPulangBinding binding;
    SPHelper spHelper;
    Integer userId;
    private CodeScanner codeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanPresensiPulangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spHelper = new SPHelper(this);
        userId = spHelper.readValue("id_guru", 0);

        codeScanner = new CodeScanner(this, binding.scannerView);

        // Parameters (default values)
        codeScanner.setCamera(CodeScanner.CAMERA_BACK);  // or CAMERA_FRONT or specific camera id
        codeScanner.setFormats(CodeScanner.ALL_FORMATS); // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.setAutoFocusMode(AutoFocusMode.SAFE); // or CONTINUOUS
        codeScanner.setScanMode(ScanMode.SINGLE);  // or CONTINUOUS or PREVIEW
        codeScanner.setAutoFocusEnabled(true); // Whether to enable auto focus or not
        codeScanner.setFlashEnabled(false); // Whether to enable flash or not

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

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                String jenis, md5;
                Long startTimestamp, expiredTimestamp, currentTimestamp;

                currentTimestamp = System.currentTimeMillis();
                String[] kode = result.getText().trim().split("_");
                jenis = kode[0];
                startTimestamp = Long.parseLong(kode[1]);
                expiredTimestamp = Long.parseLong(kode[2]);
                md5 = kode[3];

                if(!jenis.equals("pulang")){
                    runOnUiThread(() -> {
                        Toast.makeText(ScanPresensiPulangActivity.this, "Gagal : Bukan Presensi Pulang", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }else{
                    if(currentTimestamp > startTimestamp && currentTimestamp < expiredTimestamp){
                        submitPresensi("pulang", md5);
                    }else if( currentTimestamp > expiredTimestamp + (120*60*1000)){
                        runOnUiThread(() -> {
                            Toast.makeText(ScanPresensiPulangActivity.this, "Gagal : Presensi Sudah Tidak Berlaku", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                }



            }
        });

        codeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Throwable thrown) {
                runOnUiThread(() -> {
                    Toast.makeText(ScanPresensiPulangActivity.this, "Camera Initialization Error : " + thrown.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });

        binding.scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    void submitPresensi(String jenis, String kode){
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
        service.presensi(jenis, userId, kode).enqueue(new Callback<PresensiModel>() {
            @Override
            public void onResponse(Call<PresensiModel> call, Response<PresensiModel> response) {
                if(response.isSuccessful()){
                    PresensiModel presensiModel = response.body();
                    Toast.makeText(ScanPresensiPulangActivity.this, "" + presensiModel.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PresensiModel> call, Throwable t) {
                Log.d("Error Presensi", "" + t.getMessage());
                finish();
            }
        });
    }
}