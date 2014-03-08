package com.legalez.legalezapp;

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

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EnterTextActivity extends Activity {
    
    private EditText mEnterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_text);
        
        mEnterText = (EditText) findViewById(R.id.enter_text);
        
        final Button translateButton = (Button) findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("text", mEnterText.getText().toString()));
                String outPut = null;
                
                Log.d("text", mEnterText.getText().toString());
                
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://198.199.96.10/");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                     
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();               
 
                    // print response
                    outPut = EntityUtils.toString(entity);
                    Log.i("GET RESPONSEó-", outPut);
                     
                    //is = entity.getContent();
                    Log.e("log_tag ******", "good connection");
                     
                } catch (Exception e) {
                    Log.e("log_tag ******", "Error in http connection " + e.toString());
                }
            }
        });
        
        final Button clearButton = (Button) findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mEnterText.setText("");
            }
        });
    }
    
    
}
