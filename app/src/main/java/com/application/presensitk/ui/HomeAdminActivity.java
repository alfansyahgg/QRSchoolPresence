package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.databinding.ActivityHomeAdminBinding;

public class HomeAdminActivity extends AppCompatActivity {

    ActivityHomeAdminBinding binding;
    SPHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spHelper = new SPHelper(this);
        binding.tvNama.setText(spHelper.readValue("nama", "John Doe"));

        binding.cvPresensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, BuatQRActivity.class);
                startActivity(intent);
            }
        });

        binding.cvProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.cvGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, ManageGuruActivity.class);
                startActivity(intent);
            }
        });

        binding.cvLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeAdminActivity.this, AdminMenuLaporanActivity.class);
                startActivity(intent);
            }
        });


    }
}