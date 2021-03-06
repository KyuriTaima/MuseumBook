package com.epf.museumbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.epf.museumbook.Modeles.DatabaseSQLiteHelper;
import com.epf.museumbook.Modeles.Musee;

public class HomeActivity extends AppCompatActivity {
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Sets the toolbar we created with a personalized layout
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle("Home");


        Button scanBtn = (Button) findViewById(R.id.scan_button);
        Button museumBtn = (Button) findViewById(R.id.museums_button);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScanActivity.class);
                startActivity(intent);
            }
        });

        museumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(context);
                    Musee musee = db.getMusees().get(0);
                    Intent intent = new Intent(context, MuseumListActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(context, "The Museum List is still empty, try scanning a museum !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.camera_nav_icon) {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
        }
        if( id == R.id.museum_nav_icon){
            try{
                DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(this);
                Musee musee = db.getMusees().get(0);
                Intent intent = new Intent(this, MuseumListActivity.class);
                startActivity(intent);
            }catch (Exception e){
                Toast.makeText(this, "The Museum List is still empty, try scanning a museum !", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
