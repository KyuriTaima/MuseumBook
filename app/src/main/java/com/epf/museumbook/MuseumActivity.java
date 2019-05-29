package com.epf.museumbook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.epf.museumbook.Modeles.DatabaseSQLiteHelper;
import com.epf.museumbook.Modeles.Musee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MuseumActivity extends AppCompatActivity {

    private Context context;
    private Musee musee;
    private TextView title;
    private TextView address;
    private TextView fermetureAnn;
    private ImageView museumImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);
        context = this;

        title = findViewById(R.id.museum_title);
        address = findViewById(R.id.museum_address);
        fermetureAnn = findViewById(R.id.museum_femeture_annuelle);
        museumImg = findViewById(R.id.museum_image);

        if(!getIntent().getStringExtra("API_MUSEUM_ID").isEmpty()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://vps449928.ovh.net/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MuseeAPI museeAPI = retrofit.create(MuseeAPI.class);
            Call<Musee> call = museeAPI.getMusees(getIntent().getStringExtra("API_MUSEUM_ID"));
            call.enqueue(new Callback<Musee>() {
                @Override
                public void onResponse(Call<Musee> call, Response<Musee> response) {
                    if (!response.isSuccessful()) {
                        System.out.println("Error, reponse :" + response.code());
                        return;
                    } else {
                        System.out.println("API SUCCESSFUL onResponse" + response.code());
                    }
                    musee = response.body();

                    DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(context);
                    musee.setRank(db.getLastMuseumRank() + 1);
                    db.insertMusee(musee);
                    title.setText(musee.getNom());
                    address.setText(musee.getAdresse());
                    fermetureAnn.setText(musee.getFermetureAnnuelle());
                    museumImg.setImageResource(R.drawable.ic_musee_du_quai_branly);


                }

                @Override
                public void onFailure(Call<Musee> call, Throwable t) {
                    System.out.println("API Error onFailure" + t);
                }
            });
        }

        afficherMusee();
    }

    private void afficherMusee() {

    }
}
