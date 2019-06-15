package com.epf.museumbook;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.epf.museumbook.Modeles.DatabaseSQLiteHelper;
import com.epf.museumbook.Modeles.Musee;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PictureActivity extends AppCompatActivity {
    private Musee museum;
    private Context context;
    private static final int REQUEST_CAMERA = 1;
    private ImageView pictureImg;
    private Bitmap postImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        //Initialization
        context = this;
        DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(this);
        museum = db.getMusee(getIntent().getIntExtra("MUSEUM_ID", 1));
        pictureImg = findViewById(R.id.post_image);
        Button newPictureBtn = findViewById(R.id.new_picture_button);
        Button sendPictureBtn = findViewById(R.id.send_picture_button);

        newPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                    //After this function call,it will ask for permission and whether it granted or not,this response is handle in onRequestPermissionsResult() which we overrided.
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //The parameter passed in the intent tells the camera we want to take a picture and save it
                startActivityForResult(intent,0);
            }
        });
    }

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {  //If the user accepted the picture he just took, we save it and display it
            //var image: Bitmap = data?.extras?.get("data") as Bitmap
            // Get the image as Bitmap type
            postImage = (Bitmap) data.getExtras().get("data");
            //customerIv?.setImageBitmap(image)
            // Display the image
            pictureImg.setImageBitmap(postImage);

/*            //val path:String = Environment.getExternalStorageDirectory().toString()
            // Get the path to the external storage folder
            String path = Environment.getExternalStorageDirectory().toString();

            //var dir = File("$path/Pictures/")   //get the path to the Pictures folder inside the external storage
            File dir = new File(path + "/Pictures/");
            //dir.mkdirs()    //Does something important
            dir.mkdirs();
            //var file = File(dir, getPicturename())  //create a file object to store the path to the image
            File file = new File(dir, getPictureName());
            //val baos = ByteArrayOutputStream()
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);    //Compress the image to a ByteArray Object
            val photo = baos.toByteArray()

            customer.profilePictureURL = photo
            db.updateCustomer(customer) //store the image as ByteArray into the database
            var out:OutputStream
            try {
                out = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.PNG,100,out)
                out.flush()
                out.close()
            }catch (e:FileNotFoundException){
                e.printStackTrace()
                //No permission to access camera
                val permissionRequest = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE) //get the permission from the user
                requestPermissions(permissionRequest, REQUEST_CAMERA_PERMISSION)
            }*/
        }
    }

    private String getPictureName() {
        DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(context);
        return "MuseumBookPicture" + museum.getNom() + Integer.toString(museum.getImagesUrl().size() - 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please let us access your camera...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }


        }
    }

    private void startCamera() {
    }
}
