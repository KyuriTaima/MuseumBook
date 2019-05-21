package com.epf.museumbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epf.museumbook.Modeles.Musee;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MuseumListActivity extends AppCompatActivity {

    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> descriptions = new ArrayList<String>();
    private ArrayList<String> addresses = new ArrayList<String>();
    private ArrayList<Integer> ressources = new ArrayList<Integer>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_list);

        recyclerView = findViewById(R.id.museums_recycler_view);

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
                    System.out.println("API ERROR onResponse" + response.code());
                }
                Musee musee = response.body();
                    titles.add((musee.getNom()));
                    addresses.add(musee.getAdresse());
                    descriptions.add(musee.getSiteWeb());
                    ressources.add(R.drawable.ic_musee_du_quai_branly);
            }

            @Override
            public void onFailure(Call<Musee> call, Throwable t) {
                System.out.println("API Error onFailure" + t);
            }
        });

        initRecyclerView();



    }

    private void initRecyclerView() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(titles, descriptions, addresses, this, ressources);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
