package com.application.presensitk.utils.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.application.presensitk.R;
import com.application.presensitk.model.guru.GuruModel;
import com.application.presensitk.ui.InputGuruActivity;
import com.application.presensitk.ui.ManageGuruActivity;

import java.util.ArrayList;

public class ListGuruRVAdapter extends RecyclerView.Adapter<ListGuruRVAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<GuruModel> datas;
    ProgressDialog progressDialog;

    public ListGuruRVAdapter(ManageGuruActivity mContext, ArrayList<GuruModel> datas) {
        super();
        this.mContext = mContext;
        this.datas = datas;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama, tvNohp, tvEmail;
        public CardView cvGuru;
        public ImageView ivEdit, ivDelete;

        public MyViewHolder(View view) {
            super(view);
            cvGuru = itemView.findViewById(R.id.cvGuru);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNohp = itemView.findViewById(R.id.tvNohp);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            progressDialog = new ProgressDialog(mContext);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_rv_guru,parent,false);
        return new ListGuruRVAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Integer id_guru = datas.get(position).getId_guru();
        String nama = datas.get(position).getNama();
        String nohp = datas.get(position).getNohp();
        String email = datas.get(position).getEmail();
        String password = datas.get(position).getPassword();
        holder.tvNama.setText(datas.get(position).getNama());
        holder.tvEmail.setText(datas.get(position).getEmail());
        holder.tvNohp.setText(datas.get(position).getNohp());
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, InputGuruActivity.class);
                i.putExtra("id_guru",id_guru);
                i.putExtra("nama",nama);
                i.putExtra("nohp",nohp);
                i.putExtra("email",email);
                i.putExtra("password",password);
                mContext.startActivity(i);
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder((ManageGuruActivity) mContext)
                        .setMessage("Ingin menghapus ?" + id_guru)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ((ManageGuruActivity) mContext).deleteData( id_guru );
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
//        holder.cv_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(mContext,Activity_Edit.class);
//                i.putExtra("noinduk",array_noinduk.get(position));
//                i.putExtra("nama",array_nama.get(position));
//                i.putExtra("alamat",array_alamat.get(position));
//                i.putExtra("hobi",array_hobi.get(position));
//                ((Activity_Main)mContext).startActivityForResult(i,2);
//            }
//        });
//        holder.cv_main.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                new AlertDialog.Builder((Activity_Main)mContext)
//                        .setMessage("Ingin menghapus nomor induk "+array_noinduk.get(position)+" ?")
//                        .setCancelable(false)
//                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                progressDialog.setMessage("Menghapus...");
//                                progressDialog.setCancelable(false);
//                                progressDialog.show();
//
//                                AndroidNetworking.post("http://192.168.168.11/api-kompikaleng/deleteSiswa.php")
//                                        .addBodyParameter("noinduk",""+array_noinduk.get(position))
//                                        .setPriority(Priority.MEDIUM)
//                                        .build()
//                                        .getAsJSONObject(new JSONObjectRequestListener() {
//                                            @Override
//                                            public void onResponse(JSONObject response) {
//                                                progressDialog.dismiss();
//                                                try {
//                                                    Boolean status = response.getBoolean("status");
//                                                    Log.d("statuss",""+status);
//                                                    String result = response.getString("result");
//                                                    if(status){
//                                                        if(mContext instanceof Activity_Main){
//                                                            ((Activity_Main)mContext).scrollRefresh();
//                                                        }
//                                                    }else{
//                                                        Toast.makeText(mContext, ""+result, Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }catch (Exception e){
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onError(ANError anError) {
//                                                anError.printStackTrace();
//                                            }
//                                        });
//                            }
//                        })
//                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        })
//                        .show();
//                return false;
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}