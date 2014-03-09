package com.legalez.legalezapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.legalez.legalezapp.UploadImageActivity.AsyncProcessTask;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CameraImageActivity extends Activity {
    private TextView mImageName;
    String mCurrentPhotoPath;
    private Uri cameraUri;
    private String strImage;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_image);

        //dispatchTakePictureIntent();
        //galleryAddPic();
        mImageName = (TextView) findViewById(R.id.image_name_field);
        
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File out = new File(Environment.getExternalStorageDirectory(), "LegalEasy_Cam.jpg");
        cameraUri = Uri.fromFile(out);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);               
        startActivityForResult(cameraIntent, 666);
        
        final Button processImageButton = (Button) findViewById(R.id.process_image_button);
        processImageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cameraUri != null) {
                    try {
                        processImage();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    
    private void processImage() throws FileNotFoundException, IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cameraUri);
        strImage = encodeToBase64(bitmap);
        AsyncProcessTask processTask = new AsyncProcessTask();
        processTask.execute();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
    
        if (resultCode == RESULT_OK) {
            mImageName.setText(cameraUri.toString());
        } else {
            this.finish();
        }
    }
    
    public String encodeToBase64(Bitmap image) {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera_image, menu);
        return true;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = //getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                // ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
            Log.d("image", photoFile.getAbsolutePath());
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    
    public class AsyncProcessTask extends AsyncTask<String, Void, String> {
        /*private ProgressDialog dialog;

        protected void onPreExecute() {
            dialog.setMessage("Processing");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        protected void onPostExecute(Boolean result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }*/

        //private Exception exception;

        protected String doInBackground(String... urls) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("image", strImage));
            String responseText = null;
            
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://198.199.96.10/");
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

                nameValuePair.add(new BasicNameValuePair("param name",strImage));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                responseText = EntityUtils.toString(entity);
                 
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            
            Log.e("responseText", responseText);
            return responseText;
        }

        protected void onPostExecute(String output) {
            // TODO: check this.exception 
            // TODO: do something with the output
        }
    }
}
