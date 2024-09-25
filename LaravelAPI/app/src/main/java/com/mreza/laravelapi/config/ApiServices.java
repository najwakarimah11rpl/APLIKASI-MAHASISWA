
package com.example.laravelapi.config

import androidx.core.app.GrammaticalInflectionManagerCompat.GrammaticalGender
import com.example.laravelapi.model.ResponseListMahasiswa
import com.example.laravelapi.model.SubmitMahasiswa
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {
    @GET
            ("Mahasiswa")
    fun getMahasiswa(): retrofit2.Call<ResponseListMahasiswa>

    @GET("carimahasiswa")
    fun cariMahasiswa(@Query("cari") terms:String?): retrofit2.Call<ResponseListMahasiswa>

    @FormUrlEncoded
    @POST("Mahasiswa")
    fun postMahasiswa(
            @Field("namamahasiswa") namamahasiswa: String,
            @Field("nim") nim: String,
            @Field("alamat") alamat: String,
            @Field("gender") gender: String,
            @Field("agama") agama: String,
            @Field("usia") usia: String,
            ):Call<SubmitMahasiswa>
}
