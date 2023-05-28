package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.application.presensitk.databinding.ActivityMenuPresensiBinding;

public class MenuPresensiActivity extends AppCompatActivity {

    ActivityMenuPresensiBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuPresensiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cvHadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPresensiActivity.this, ScanPresensiHadirActivity.class);
                startActivity(intent);
            }
        });

        binding.cvPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPresensiActivity.this, ScanPresensiPulangActivity.class);
                startActivity(intent);
            }
        });

        binding.cvIzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPresensiActivity.this, PresensiIzinActivity.class);
                startActivity(intent);
            }
        });
    }
}