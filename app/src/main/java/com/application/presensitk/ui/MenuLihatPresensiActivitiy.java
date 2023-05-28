package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.application.presensitk.R;
import com.application.presensitk.databinding.ActivityMenuLihatPresensiBinding;

public class MenuLihatPresensiActivitiy extends AppCompatActivity {

    ActivityMenuLihatPresensiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuLihatPresensiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cvHadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuLihatPresensiActivitiy.this, GuruLihatPresensiActivity.class);
                intent.putExtra("jenis", "hadir");
                startActivity(intent);
            }
        });

        binding.cvPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuLihatPresensiActivitiy.this, GuruLihatPresensiActivity.class);
                intent.putExtra("jenis", "pulang");
                startActivity(intent);
            }
        });

        binding.cvTerlambat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuLihatPresensiActivitiy.this, GuruLihatPresensiActivity.class);
                intent.putExtra("jenis", "terlambat");
                startActivity(intent);
            }
        });

        binding.cvIzin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuLihatPresensiActivitiy.this, GuruLihatIzinActivity.class);
                intent.putExtra("jenis", "izin");
                startActivity(intent);
            }
        });
    }
}