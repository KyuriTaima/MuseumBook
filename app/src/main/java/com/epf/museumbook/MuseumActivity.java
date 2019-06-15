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

/*
 * This activity shows the information about a particular museum
 * It is accessed via the MuseumListActivity and will show a locally stored museum
 * Or it can be accessed after a user scanned a QRCode and it will get the information from the API
 */


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

        //Get all the layouts objects
        title = findViewById(R.id.museum_title);
        address = findViewById(R.id.museum_address);
        fermetureAnn = findViewById(R.id.museum_femeture_annuelle);
        museumImg = findViewById(R.id.museum_image);
        addImageBtn = findViewById(R.id.add_picture_button);

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PictureActivity.class);
                intent.putExtra("MUSEUM_ID", musee.getRank());
                startActivity(intent);

            }
        });



        //This is a try/catch because if the user came from the MuseumListActivity, it will throw an exception and the code
        //Will not be executed
        try {
            getIntent().getStringExtra("API_MUSEUM_ID").isEmpty();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://vps449928.ovh.net/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MuseeAPI museeAPI = retrofit.create(MuseeAPI.class);
            Call<Musee> call = museeAPI.getMusees(getIntent().getStringExtra("API_MUSEUM_ID"));

            /*
             * The logic here is to get the museum id from the API, then build a museum object with the data we got from the API
             * Afterwards, we send a second request to the API passing the museum id and getting all the pictures from this museum
             */
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

                    title.setText(musee.getNom());
                    address.setText(musee.getAdresse());
                    fermetureAnn.setText(musee.getFermetureAnnuelle());

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://vps449928.ovh.net/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MuseeAPI museeAPI = retrofit.create(MuseeAPI.class);

                    Call<ArrayList<String>> imageCall = museeAPI.getImages(getIntent().getStringExtra("API_MUSEUM_ID"));
                    imageCall.enqueue(new Callback<ArrayList<String>>() {
                        @Override
                        public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                            try {
                                imagesUrl = response.body();
                                Glide.with(context)
                                        .load(imagesUrl.get(0))
                                        .into(museumImg);
                            }catch(Exception e){
                                setMusee();
                            }
                            setMusee();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                            setMusee();
                        }
                    });


                }

                @Override
                public void onFailure(Call<Musee> call, Throwable t) {
                }
            });
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        /*
         * This try/catch will catch an exception if the user came from the ScanActivity and not the MuseumListActivity
         * The logic here is to get the museum rank that was passed in intent and then get all the information from the database
         */

        try {
            DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(this);
            musee = db.getMusee(getIntent().getIntExtra("MUSEUM_ID", -8));

            title.setText(musee.getNom());
            address.setText(musee.getAdresse());
            fermetureAnn.setText(musee.getFermetureAnnuelle());
            imagesUrl = db.getImages(musee);

            Glide.with(context)
                    .load(imagesUrl.get(0))
                    .into(museumImg);
            setMusee();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }





    }

    private void setMusee() {
        musee.setImagesUrl(imagesUrl);
        DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(context);
        db.insertMusee(musee);
    }
}
