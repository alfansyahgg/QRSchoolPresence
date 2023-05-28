package com.application.presensitk.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.databinding.ActivityHomeAdminBinding;
import com.application.presensitk.databinding.ActivityHomeGuruBinding;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class HomeGuruActivity extends AppCompatActivity  {

    ActivityHomeGuruBinding binding;
    SPHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeGuruBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        spHelper = new SPHelper(this);
        binding.tvNama.setText(spHelper.readValue("nama", "John Doe"));

        binding.cvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeGuruActivity.this, MenuPresensiActivity.class);
                startActivity(intent);
            }
        });

        binding.cvKehadiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeGuruActivity.this, MenuLihatPresensiActivitiy.class);
                startActivity(intent);
            }
        });

        binding.cvProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeGuruActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.cvKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeGuruActivity.this, LoginActivity.class);
                spHelper.saveValue("login", false);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            }
        });
    }


}