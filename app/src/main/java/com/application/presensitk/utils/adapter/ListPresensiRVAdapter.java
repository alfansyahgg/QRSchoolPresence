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
import com.application.presensitk.model.guru.GuruModel;
import com.application.presensitk.model.presensi.DetailPresensiModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListPresensiRVAdapter extends RecyclerView.Adapter<ListPresensiRVAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<DetailPresensiModel> datas;
    ProgressDialog progressDialog;

    public ListPresensiRVAdapter(Context mContext, ArrayList<DetailPresensiModel> datas) {
        super();
        this.mContext = mContext;
        this.datas = datas;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTanggal, tvJam;
        public CardView cvMain;
        public ImageView ivIcon, ivDelete;

        public MyViewHolder(View view) {
            super(view);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJam = itemView.findViewById(R.id.tvJam);
            cvMain = itemView.findViewById(R.id.cvGuru);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_rv_presensi,parent,false);
        return new ListPresensiRVAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Integer id_presensi = datas.get(position).getId_presensi();
        Integer id_admin = datas.get(position).getId_admin();
        Integer id_guru = datas.get(position).getId_guru();
        String waktu = datas.get(position).getWaktu();
        String tipe = datas.get(position).getTipe();
        String[] wkt = waktu.split(" ");

        try {
            SimpleDateFormat dateFormatprev = new SimpleDateFormat("yyyy-MM-dd");
            Date d = dateFormatprev.parse(wkt[0]);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMM yyyy");
            String changedDate = dateFormat.format(d);
            holder.tvTanggal.setText(changedDate);
            holder.tvJam.setText(wkt[1]);
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
