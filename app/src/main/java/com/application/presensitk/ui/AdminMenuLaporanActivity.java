package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.application.presensitk.R;
import com.application.presensitk.databinding.ActivityAdminMenuLaporanBinding;

public class AdminMenuLaporanActivity extends AppCompatActivity {

    ActivityAdminMenuLaporanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMenuLaporanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cvSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuLaporanActivity.this, AdminLihatPresensiActivity.class);
                intent.putExtra("jenis", "semua");
                startActivity(intent);
            }
        });

        binding.cvHadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuLaporanActivity.this, AdminLihatPresensiActivity.class);
                intent.putExtra("jenis", "hadir");
                startActivity(intent);
            }
        });

        binding.cvPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuLaporanActivity.this, AdminLihatPresensiActivity.class);
                intent.putExtra("jenis", "pulang");
                startActivity(intent);
            }
        });

        binding.cvTerlambat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuLaporanActivity.this, AdminLihatPresensiActivity.class);
                intent.putExtra("jenis", "terlambat");
                startActivity(intent);
            }
        });

        binding.cvIzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMenuLaporanActivity.this, AdminLihatIzinActivity.class);
                intent.putExtra("jenis", "izin");
                startActivity(intent);
            }
        });
    }
}