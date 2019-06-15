package com.epf.museumbook;

import android.graphics.Bitmap;

import com.epf.museumbook.Modeles.Musee;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/*
 * This interface has all the routes needed for the application to call the API
 */


public interface MuseeAPI {

    @GET("musees/{id}")
    Call<Musee> getMusees(@Path("id") String id);

    @GET("musees/{id}/pictures/{id_pictures}")
    Call<Bitmap> getImage(@Path("id") String id, String image_id);

    @GET("musees/{id}/pictures")
    Call<ArrayList<String>> getImages(@Path("id") String id);

    @Multipart
    @POST("musees/{id}/pictures")
    Call<String> uploadImage(@Part MultipartBody.Part file, @Path("id") String id);

}
