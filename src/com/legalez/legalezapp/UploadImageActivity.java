package com.legalez.legalezapp;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UploadImageActivity extends Activity {

    private static final int SELECT_PICTURE = 0;
    private EditText mImageName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        
        mImageName = (EditText) findViewById(R.id.image_name_field);
        
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
                
                Bitmap bitmapOrg = BitmapFactory.decodeFile(mImageName.getText().toString());
                Log.d("image", mImageName.getText().toString());
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                 
                //Here you can define .PNG as well
                bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 95, bao);
                byte[] ba = bao.toByteArray();
                String ba1 = Base64.encodeToString(ba, 0);
                 
                System.out.println("uploading image now ——–" + ba1);
                 
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("image", ba1));
                
                String output = null;
                
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://198.199.96.10/");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                     
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();               
 
                    // print response
                    output = EntityUtils.toString(entity);
                    Log.i("GET RESPONSE—-", output);
                     
                    //is = entity.getContent();
                    Log.e("log_tag ******", "good connection");
                     
                    bitmapOrg.recycle();
                     
                } catch (Exception e) {
                    Log.e("log_tag ******", "Error in http connection " + e.toString());
                }
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
    
        if (resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            mImageName.setText(selectedImage.toString());
        }
    }

}
