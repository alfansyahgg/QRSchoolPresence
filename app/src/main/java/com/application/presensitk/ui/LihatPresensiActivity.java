package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.application.presensitk.R;
import com.application.presensitk.databinding.ActivityLihatPresensiBinding;

public class LihatPresensiActivity extends AppCompatActivity {

    ActivityLihatPresensiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLihatPresensiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}