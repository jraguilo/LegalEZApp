package com.legalez.legalezapp;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UploadImageActivity extends Activity {

    private static final int SELECT_PICTURE = 0;
    private TextView mImageName;
    private Uri selectedImage;
    private String strImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        
        mImageName = (TextView) findViewById(R.id.image_name_field);
        
        final Button selectImageButton = (Button) findViewById(R.id.select_image_button);
        selectImageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent newInt = new Intent();
                newInt.setType("image/*");
                newInt.setAction(Intent.ACTION_PICK);
                startActivityForResult(newInt, SELECT_PICTURE);
            }
        });
        
        final Button processImageButton = (Button) findViewById(R.id.process_image_button);
        processImageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selectedImage != null) {
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
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
        strImage = encodeToBase64(bitmap);
        AsyncProcessTask processTask = new AsyncProcessTask();
        processTask.execute();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
    
        if (resultCode == RESULT_OK) {
            selectedImage = imageReturnedIntent.getData();
            mImageName.setText(selectedImage.toString());
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

    public class AsyncProcessTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

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
        }

        private Exception exception;

        protected String doInBackground(String... urls) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("image", strImage));
            String output = null;
            
            Log.d("text", strImage);
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://www.URL...");
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

                nameValuePair.add(new BasicNameValuePair("param name",strImage));

                HttpResponse response = httpclient.execute(httppost);
                  HttpEntity entity = response.getEntity();
                   String responseText = EntityUtils.toString(entity);
                 
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            
            return output;
        }

        protected void onPostExecute(String output) {
            // TODO: check this.exception 
            // TODO: do something with the output
        }

    }
    
    
}
