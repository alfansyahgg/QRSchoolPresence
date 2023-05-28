package com.application.presensitk.utils.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.presensitk.R;
import com.application.presensitk.model.presensi.DetailAdminPresensiModel;
import com.application.presensitk.model.presensi.DetailPresensiModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListAdminPresensiRVAdapter extends RecyclerView.Adapter<ListAdminPresensiRVAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<DetailAdminPresensiModel> datas;
    ProgressDialog progressDialog;

    public ListAdminPresensiRVAdapter(Context mContext, ArrayList<DetailAdminPresensiModel> datas) {
        super();
        this.mContext = mContext;
        this.datas = datas;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTanggal, tvTipe, tvNama;
        public CardView cvMain;
        public ImageView ivIcon, ivDelete;

        public MyViewHolder(View view) {
            super(view);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvTipe = itemView.findViewById(R.id.tvTipe);
            cvMain = itemView.findViewById(R.id.cvGuru);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    @Override
    public ListAdminPresensiRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_rv_admin_presensi,parent,false);
        return new ListAdminPresensiRVAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdminPresensiRVAdapter.MyViewHolder holder, int position) {
        Integer id_presensi = datas.get(position).getId_presensi();
        Integer id_admin = datas.get(position).getId_admin();
        Integer id_guru = datas.get(position).getId_guru();
        String waktu = datas.get(position).getWaktu();
        String tipe = datas.get(position).getTipe();
        String nama = datas.get(position).getNama();

        try {
            SimpleDateFormat dateFormatprev = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = dateFormatprev.parse(waktu);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            String changedDate = dateFormat.format(d);
            holder.tvNama.setText(nama);
            holder.tvTanggal.setText(changedDate);
            holder.tvTipe.setText(tipe.toUpperCase(Locale.ROOT));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(tipe.equals("hadir")){
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_arrow_upward_24));
        }else if(tipe.equals("pulang")){
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_arrow_downward_24));
        }else if(tipe.equals("terlambat")){
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_remove_circle_24));
        }else{
            holder.ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_info_24));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
