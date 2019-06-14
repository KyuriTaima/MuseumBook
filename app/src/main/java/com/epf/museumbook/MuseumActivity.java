package com.epf.museumbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.epf.museumbook.Modeles.DatabaseSQLiteHelper;
import com.epf.museumbook.Modeles.Musee;

import java.util.ArrayList;
import java.util.List;

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
    private Button addImageBtn;
    private ArrayList<String> imagesUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);
        context = this;

        title = findViewById(R.id.museum_title);
        address = findViewById(R.id.museum_address);
        fermetureAnn = findViewById(R.id.museum_femeture_annuelle);
        museumImg = findViewById(R.id.museum_image);
        addImageBtn = findViewById(R.id.add_picture_button);

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureActivity.class);

            }
        });



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

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://vps449928.ovh.net/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MuseeAPI museeAPI = retrofit.create(MuseeAPI.class);

                    Call<ArrayList<String>> imageCall = museeAPI.getImages(getIntent().getStringExtra("API_MUSEUM_ID"));
                    imageCall.enqueue(new Callback<ArrayList<String>>() {
                        @Override
                        public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                            Glide.with(context)
                                    .load(response.body().get(0))
                                    .into(museumImg);
                            DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(context);
                            imagesUrl = response.body();
                            setMusee();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                            System.out.println("Error on image API call");
                        }
                    });


                }

                @Override
                public void onFailure(Call<Musee> call, Throwable t) {
                    System.out.println("API Error onFailure" + t);
                }
            });





        }




    }

    private void setMusee() {
        musee.setImagesUrl(imagesUrl);
        DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(context);
        db.insertMusee(musee);
    }
}
