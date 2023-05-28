package com.application.presensitk.utils.adapter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.application.presensitk.R;
import com.application.presensitk.config.StaticStuffs;
import com.application.presensitk.model.presensi.DetailIzinModel;
import com.application.presensitk.model.presensi.DetailPresensiModel;
import com.application.presensitk.service.ApiService;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kotlin.UByteArray;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListIzinRVAdapter extends RecyclerView.Adapter<ListIzinRVAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<DetailIzinModel> datas;
    ProgressDialog progressDialog;

    public ListIzinRVAdapter(Context mContext, ArrayList<DetailIzinModel> datas) {
        super();
        this.mContext = mContext;
        this.datas = datas;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTanggal, tvAlasan, tvFile;
        public CardView cvMain;
        public ImageView ivIcon, ivDelete;

        public MyViewHolder(View view) {
            super(view);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvAlasan = itemView.findViewById(R.id.tvAlasan);
            tvFile = itemView.findViewById(R.id.tvFile);
            cvMain = itemView.findViewById(R.id.cvGuru);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    @Override
    public ListIzinRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_rv_izin,parent,false);
        return new ListIzinRVAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListIzinRVAdapter.MyViewHolder holder, int position) {
        Integer id_presensi = datas.get(position).getId_presensi();
        Integer id_admin = datas.get(position).getId_admin();
        Integer id_guru = datas.get(position).getId_guru();
        String waktu = datas.get(position).getWaktu();
        String alasan = datas.get(position).getAlasan();
        String tipe = datas.get(position).getTipe();
        String file = datas.get(position).getFile();
        String[] wkt = waktu.split(" ");

        try {
            SimpleDateFormat dateFormatprev = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = dateFormatprev.parse(waktu);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            String changedDate = dateFormat.format(d);
            holder.tvTanggal.setText(changedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvAlasan.setText(alasan);

        holder.tvFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                String url = StaticStuffs.BASE_URL_FILE + file;
                Call<ResponseBody> respon =  service.getFileIzin(url);
                respon.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            String fileName= url.substring(url.lastIndexOf("/")+1);
                            if(saveFile(mContext, response.body(), fileName)){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    makeNotification(fileName);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Err Download", "" + t.getMessage());
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private Boolean saveFile(Context context, ResponseBody body, String fileName) {
        if (body == null) {
            return false;
        }

        InputStream input;
        try {
            input = body.byteStream();
//            String path = Environment.getExternalStorageDirectory().getPath();
            String path = mContext.getExternalFilesDir("izin").getPath();
            FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
            IOUtils.write(body.bytes(), fos);
        } catch (Exception e) {
            Log.e("saveFile", e.toString());
            return false;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void makeNotification(String fileName){
        NotificationChannel notificationChannel;
        NotificationManager notificationManager;
        Notification.Builder builder;
        String channelId = "i.presensitk.download.izin";
        String description = "Notifikasi Download File Izin";
        notificationManager = mContext.getSystemService(NotificationManager.class);

        Intent intent = openFile(fileName);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_MUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            long[] vibPattern = new long[9];
            vibPattern[0] = 100;
            vibPattern[1] = 200;
            vibPattern[2] = 300;
            vibPattern[3] = 400;
            vibPattern[4] = 500;
            vibPattern[5] = 400;
            vibPattern[6] = 300;
            vibPattern[7] = 200;
            vibPattern[7] = 400;
            notificationChannel.setVibrationPattern(
                    vibPattern
            );
            notificationManager.createNotificationChannel(notificationChannel);

            builder = new Notification.Builder(mContext, channelId)
                    .setSmallIcon(R.drawable.ic_baseline_info_24)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_background))
                    .setContentTitle("Download Berhasil")
                    .setContentText("Klik Untuk Membuka File")
                    .setSubText(fileName)
                    .setContentIntent(pendingIntent);
        } else {
            builder = new Notification.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_baseline_info_24)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_background))
                    .setContentTitle("Download Berhasil")
                    .setContentText("Klik Untuk Membuka File")
                    .setSubText(fileName)
                    .setContentIntent(pendingIntent);
        }
        notificationManager.notify(1234, builder.build());
    }

    private Intent openFile(String fileName){
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String type = "application/" + extension;
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ "/Presensi TK";
        String path = mContext.getExternalFilesDir("izin").getPath();
        File file = new File("" + path + "/" + fileName);
        Uri uri = FileProvider.getUriForFile(mContext.getApplicationContext(), mContext.getApplicationContext().getPackageName() + ".provider", file);

        Log.e("cek open", "" + uri);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, type);
        Intent chooser = Intent.createChooser(intent, "Open File With");
        return chooser;
    }
}
