package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityGuruLihatIzinBinding;
import com.application.presensitk.model.presensi.IzinModel;
import com.application.presensitk.model.presensi.PresensiModel;
import com.application.presensitk.service.ApiService;
import com.application.presensitk.utils.adapter.ListIzinRVAdapter;
import com.application.presensitk.utils.adapter.ListPresensiRVAdapter;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GuruLihatIzinActivity extends AppCompatActivity {

    ActivityGuruLihatIzinBinding binding;
    SPHelper spHelper;
    String jenis;
    ListIzinRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuruLihatIzinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spHelper = new SPHelper(this);

        Intent intent = getIntent();
        if(intent != null){
            jenis = intent.getStringExtra("jenis");
        }

        binding.rvIzin.setHasFixedSize(true);

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
        service.readIzin(jenis, spHelper.readValue("id_guru", 0)).enqueue(new Callback<IzinModel>() {
            @Override
            public void onResponse(Call<IzinModel> call, Response<IzinModel> response) {
                if(response.isSuccessful()){
                    IzinModel izinModel = response.body();
                    Toast.makeText(GuruLihatIzinActivity.this, "" + izinModel.getMessage(), Toast.LENGTH_SHORT).show();
                    if(izinModel.getData() != null){
                        if(izinModel.getData().size() > 0){
                            adapter = new ListIzinRVAdapter(GuruLihatIzinActivity.this, izinModel.getData());
                            binding.rvIzin.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<IzinModel> call, Throwable t) {
                Toast.makeText(GuruLihatIzinActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}