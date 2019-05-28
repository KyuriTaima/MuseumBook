package com.epf.museumbook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.epf.museumbook.Modeles.DatabaseSQLiteHelper;
import com.epf.museumbook.Modeles.Musee;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScanActivity extends Activity implements ZXingScannerView.ResultHandler {
    Context context = this;
    private ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please let us access your camera...", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //If the permission is not  already granted then ask,if it is already granted then just do nothing for the permission section
        if (!(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            //After this function call,it will ask for permission and whether it granted or not,this response is handle in onRequestPermissionsResult() which we overrided.
        }

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);



    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        callAPI(rawResult.getText());
    }

    private void callAPI(String id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vps449928.ovh.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MuseeAPI museeAPI = retrofit.create(MuseeAPI.class);
        Call<Musee> call = museeAPI.getMusees(id);
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

                DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(context);
                musee.setRank(db.getLastMuseumRank() + 1);
                db.insertMusee(musee);

            }

            @Override
            public void onFailure(Call<Musee> call, Throwable t) {
                System.out.println("API Error onFailure" + t);
            }
        });
    }
}
