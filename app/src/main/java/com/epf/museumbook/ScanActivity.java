package com.epf.museumbook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epf.museumbook.Modeles.DatabaseHelper;
import com.epf.museumbook.Modeles.Musee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScanActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vps449928.ovh.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MuseeAPI museeAPI = retrofit.create(MuseeAPI.class);
        Call<Musee> call = museeAPI.getMusees("5c637e3c61e55c808b31e1ae12a57fc5c4842b4b");
        call.enqueue(new Callback<Musee>() {
            @Override
            public void onResponse(Call<Musee> call, Response<Musee> response) {
                if (!response.isSuccessful()){
                    System.out.println("Error, reponse :" + response.code());
                    return;
                }
                else{
                    System.out.println("API SUCCESSFUL onResponse" + response.code());
                }
                Musee musee = response.body();
                DatabaseHelper db = new DatabaseHelper(context);
                db.insertMusee(musee);

            }

            @Override
            public void onFailure(Call<Musee> call, Throwable t) {
                System.out.println("API Error onFailure" + t);
            }
        });

    }
}
