package com.epf.museumbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MuseumListActivity extends AppCompatActivity {

    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> descriptions = new ArrayList<String>();
    private ArrayList<String> addresses = new ArrayList<String>();
    private ArrayList<Integer> ressources = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_list);

        titles.add("Musée Rodin");
        titles.add("Musée du Louvre");
        titles.add("Musée du Quai Branly");

        descriptions.add("Musée de sculptures");
        descriptions.add("Musée d'art et d'histoire");
        descriptions.add("Musée d'art contemporain");

        addresses.add("Jardin du Luxembourg");
        addresses.add("Explanade du Louvre");
        addresses.add("Quai Branly");

        ressources.add(R.drawable.ic_musee_rodin);
        ressources.add(R.drawable.ic_musee_du_louvre);
        ressources.add(R.drawable.ic_musee_du_quai_branly);

        initRecyclerView();



    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.museums_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(titles, descriptions, addresses, this, ressources);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
