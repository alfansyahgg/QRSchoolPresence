package com.application.presensitk.service;

import com.application.presensitk.model.auth.LoginModel;
import com.application.presensitk.model.guru.GuruModel;
import com.application.presensitk.model.guru.GuruResponseModel;
import com.application.presensitk.model.presensi.AdminIzinModel;
import com.application.presensitk.model.presensi.AdminPresensiModel;
import com.application.presensitk.model.presensi.DetailAdminPresensiModel;
import com.application.presensitk.model.presensi.IzinModel;
import com.application.presensitk.model.presensi.PresensiModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @POST("login.php")
    @FormUrlEncoded
    Call<LoginModel> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("presensi.php")
    @FormUrlEncoded
    Call<PresensiModel> presensi(
            @Field("jenis") String jenis,
            @Field("id_guru") Integer id_guru,
            @Field("kode") String kode
    );

    @Multipart
    @POST("izin.php")
    Call<PresensiModel> izin(
            @Part("id_guru") RequestBody id_guru,
            @Part("alasan") RequestBody alasan,
            @Part MultipartBody.Part file
            );

    @GET("guru_read.php")
    Call<GuruResponseModel> readGuru();

    @GET("guru_show.php")
    Call<GuruResponseModel> showGuru(
            @Query("id_guru") Integer id_guru
    );

    @POST("guru_create.php")
    @FormUrlEncoded
    Call<GuruResponseModel> createGuru(
            @Field("nama") String nama,
            @Field("nohp") String nohp,
            @Field("email") String email,
            @Field("password") String password
        );

    @POST("guru_update.php")
    @FormUrlEncoded
    Call<GuruResponseModel> updateGuru(
            @Field("id_guru") Integer id_guru,
            @Field("nama") String nama,
            @Field("nohp") String nohp,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("guru_delete.php")
    @FormUrlEncoded
    Call<GuruResponseModel> deleteGuru(
            @Field("id_guru") Integer id_guru
    );

    @GET("read_presensi.php")
    Call<PresensiModel> readPresensi(
            @Query("jenis") String jenis,
            @Query("id_guru") Integer id_guru
    );

    @GET("read_presensi.php")
    Call<IzinModel> readIzin(
            @Query("jenis") String jenis,
            @Query("id_guru") Integer id_guru
    );

    @GET("read_presensi.php")
    Call<AdminPresensiModel> adminReadPresensi(
            @Query("jenis") String jenis,
            @Query("tanggal") String tanggal
    );

    @GET("read_presensi.php")
    Call<AdminIzinModel> adminReadIzin(
            @Query("jenis") String jenis,
            @Query("tanggal") String tanggal
    );

    @POST("ubah_profil.php")
    @FormUrlEncoded
    Call<GuruResponseModel> ubahProfil(
            @Field("id_guru") Integer id_guru,
            @Field("nama") String nama,
            @Field("nohp") String nohp,
            @Field("email") String email,
            @Field("password") String password,
            @Field("tipe") String tipe
    );

    @GET
    Call<ResponseBody> getFileIzin(@Url String url);
}
