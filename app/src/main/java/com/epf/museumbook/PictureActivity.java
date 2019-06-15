package com.epf.museumbook;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PictureActivity extends AppCompatActivity {
    private Musee museum;
    private Context context;
    private static final int REQUEST_CAMERA = 1;
    private ImageView pictureImg;
    private Bitmap postImage;
    private String mCurrentPhotpPath;

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
            Uri image = getImageUri(this, postImage);
            String filePath = getRealPathFromURIPath(image, context);
            File file = new File(filePath);

            RequestBody mFile = RequestBody.create(MediaType.parse("/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), mFile);


/*           //val path:String = Environment.getExternalStorageDirectory().toString()
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
            postImage.compress(Bitmap.CompressFormat.PNG, 100, baos);    //Compress the image to a ByteArray Object
            byte[] photo = baos.toByteArray();

            //customer.profilePictureURL = photo
            //db.updateCustomer(customer) //store the image as ByteArray into the database
            OutputStream out;
            try {
                out = new FileOutputStream(file);
                postImage.compress(Bitmap.CompressFormat.PNG,100,out);
                out.flush();
                out.close();
            }catch (FileNotFoundException e) {
                System.out.println(e.getMessage() + "PictureActivity onActivityResult FileNotFoundException");
            }catch (IOException e){
                System.out.println(e.getMessage() + "PictureActivity onActivityResult IOException");
            }*/

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://vps449928.ovh.net/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MuseeAPI museeAPI = retrofit.create(MuseeAPI.class);

            Call<String> call = museeAPI.uploadImage(body, museum.getId());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    System.out.println("on Response in PictureActivity");
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    System.out.println("on Failuref in PictureActivity");
                }
            });

        }
    }

    private String getRealPathFromURIPath(Uri uri, Context context) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        /* DocumentProvider */
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private Uri getImageUri(PictureActivity pictureActivity, Bitmap postImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        postImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), postImage, "Title", null);
        return Uri.parse(path);
    }

    private String getPictureName() {
        DatabaseSQLiteHelper db = new DatabaseSQLiteHelper(context);
        return "MuseumBookPicture" + museum.getId() + Integer.toString(museum.getImagesUrl().size() - 1);
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


    //create image name
    private File createImageFile() throws IOException {
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="JPGE"+timeStamp+"_";
        File storgeDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storgeDir);
        mCurrentPhotpPath="file:"+image.getAbsolutePath();
        return image;
    }


    //save image
    private void galleryAddpic(){
        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f=new File(mCurrentPhotpPath);
        Uri contentUri=Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }
}
