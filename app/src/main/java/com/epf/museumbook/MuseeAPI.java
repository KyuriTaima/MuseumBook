package com.epf.museumbook;

import com.epf.museumbook.Modeles.Musee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MuseeAPI {

    @GET("musees/{id}")
    Call<Musee> getMusees(@Path("id") String id);

    //@GET("musees/{id}/pictures/{id_pictures}")
    //Call<Byte> getImage(@Path("id") String id, String image_id);
}
