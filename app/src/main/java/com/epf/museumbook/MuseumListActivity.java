package com.epf.museumbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epf.museumbook.Modeles.Musee;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_list);
    }



    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.museums_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(titles, descriptions, addresses, this, ressources);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
