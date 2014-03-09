package com.legalez.legalezapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
    
    public class AsyncProcessTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("image", strImage));
            String responseText = null;
            
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://198.199.96.10/upload/");
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

                nameValuePair.add(new BasicNameValuePair("image",strImage));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                responseText = EntityUtils.toString(entity);
                
                //display response text in enter text activity
                Intent newInt = new Intent(CameraImageActivity.this, EnterTextActivity.class);
                newInt.putExtra("text", responseText);
                startActivity(newInt);
                 
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            
            Log.e("responseText", responseText);
            return responseText;
        }
    }
}
