package com.application.presensitk.ui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityBuatQractivityBinding;
import com.google.zxing.WriterException;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class BuatQRActivity extends AppCompatActivity {

    ActivityBuatQractivityBinding binding;
    SPHelper spHelper;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String jenis;
    Long selectedTimestamp = null, expiredTimestamp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuatQractivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spHelper = new SPHelper(this);

        binding.btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(BuatQRActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                c.set(Calendar.HOUR_OF_DAY, mHour);
                                c.set(Calendar.MINUTE, mMinute);
                                c.set(Calendar.SECOND, mMinute = c.get(Calendar.SECOND));
                                selectedTimestamp = c.getTimeInMillis();

//                                binding.tvTanggalPresensi.setText("Tanggal Presensi : " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " " + mHour + ":" + mMinute);
                                String selectedDatetime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").
                                        format(new java.util.Date(selectedTimestamp));
                                binding.tvTanggalPresensi.setText("Tanggal Presensi : " + selectedDatetime);

                                expiredTimestamp = selectedTimestamp + (60*60*1000);
                                String expiredDatetime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").
                                        format(new java.util.Date(expiredTimestamp));
                                binding.tvExpired.setText("Berlaku Sampai : " + expiredDatetime);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        binding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buatQr();
            }
        });
    }

    void buatQr(){
        if((!binding.rbHadir.isChecked() && !binding.rbPulang.isChecked()) || selectedTimestamp == null){
            Toast.makeText(BuatQRActivity.this, "Invalid: Isi Jenis Presensi dan Tanggal Presensi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.rbHadir.isChecked()){
            jenis = "hadir";
        }else{
            jenis = "pulang";
        }

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;

        String kode = jenis + "_" + selectedTimestamp + "_" + expiredTimestamp + "_" + MD5("presensitk");

        QRGEncoder qrgEncoder = new QRGEncoder(kode, null, QRGContents.Type.TEXT, smallerDimension);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            binding.ivQrcode.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch(UnsupportedEncodingException ex){
        }
        return null;
    }
}