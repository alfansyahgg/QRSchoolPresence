package com.application.presensitk.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.application.presensitk.R;
import com.application.presensitk.config.SPHelper;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.databinding.ActivityPresensiIzinBinding;
import com.application.presensitk.model.presensi.PresensiModel;
import com.application.presensitk.service.ApiService;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class PresensiIzinActivity extends AppCompatActivity {

    private static final int PICK_FILE = 99;
    ActivityPresensiIzinBinding binding;
    String waktu;
    SPHelper spHelper;
    int userId;

    Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPresensiIzinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spHelper = new SPHelper(this);
        userId = spHelper.readValue("id_guru", 0);



        DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        waktu = df.format(Calendar.getInstance().getTime());
        binding.tvTanggalIzin.setText("Tanggal Izin : " + waktu);


        binding.btnLoadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        binding.btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alasan = binding.etAlasan.getText().toString().trim();
                if(!alasan.isEmpty() && mUri != null){
                    uploadIzin(mUri, alasan);
                }else{
                    Toast.makeText(PresensiIzinActivity.this, "Invalid!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // SELECT_PICTURE constant
            if (requestCode == PICK_FILE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    mUri = selectedImageUri;
                    String filePath = getRealPathFromURI(mUri);
                    File file = new File(filePath);
                    binding.tvFile.setText(file.getName());
                    Toast.makeText(this, "" + mUri, Toast.LENGTH_SHORT).show();
                    // update the preview image in the layout
                }
            }
        }
    }

    void uploadIzin(Uri contentUri, String alasan){
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

        MultipartBody.Part fileBody;
        String filePath = getRealPathFromURI(mUri);
        File file = new File(filePath);


        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        fileBody = MultipartBody.Part.createFormData("file", file.getName(), mFile);


        RequestBody rUserId =
                RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(userId));
        RequestBody rAlasan =
                RequestBody.create(MediaType.parse("multipart/form-data"), alasan);


        ApiService service = retrofit.create(ApiService.class);
        service.izin(rUserId, rAlasan, fileBody).enqueue(new Callback<PresensiModel>() {
            @Override
            public void onResponse(Call<PresensiModel> call, Response<PresensiModel> response) {
                if(response.isSuccessful()){
                    PresensiModel presensiModel = response.body();
                    Toast.makeText(PresensiIzinActivity.this, "" + presensiModel.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PresensiModel> call, Throwable t) {
                Log.d("Error Izin", "" + t.getMessage());
            }
        });
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        i.setType("image/*");

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_FILE);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}