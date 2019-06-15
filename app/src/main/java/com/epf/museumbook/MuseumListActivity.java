package com.epf.museumbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epf.museumbook.Modeles.DatabaseSQLiteHelper;
import com.epf.museumbook.Modeles.Musee;

import java.util.ArrayList;

public class MuseumListActivity extends AppCompatActivity {

    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> descriptions = new ArrayList<String>();
    private ArrayList<String> addresses = new ArrayList<String>();
    private ArrayList<String> ressources = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_list);

        DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(this);
        ArrayList<Musee> musees = new ArrayList<>();
        musees = db.getMusees();

        for(int i=0;i<musees.size();i++){
            titles.add(musees.get(i).getNom());
            descriptions.add(musees.get(i).getSiteWeb());
            addresses.add(musees.get(i).getAdresse());
            try {
                ressources.add(musees.get(i).getImagesUrl().get(0));
            }catch(Exception e){
                System.out.println(e.getMessage() + "///no picture found");
            }
        }
        initRecyclerView();
    }



    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.museums_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(titles, descriptions, addresses, this, ressources);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
