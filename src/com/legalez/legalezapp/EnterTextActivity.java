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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
        Bundle extras = getIntent().getExtras();
        if (getIntent().hasExtra("text")) {
            mEnterText.setText(extras.getString("text"));
        }
        
        final Button translateButton = (Button) findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UploadTextTask textTask = new UploadTextTask();
                textTask.execute();
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
    
    class UploadTextTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            Log.d("text", "Performing UploadText");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("block", mEnterText.getText().toString()));
            String output = null;
            
            Log.d("text", mEnterText.getText().toString());
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://198.199.96.10/translatesimple/");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.d("Post_Tag", httppost.toString());
                 
                HttpResponse response = httpclient.execute(httppost);
                Log.d("Response", response.getStatusLine().toString());
                HttpEntity entity = response.getEntity();               

                // print response
                output = EntityUtils.toString(entity);
                Log.i("GET RESPONSE--", output);
                 
                //is = entity.getContent();
                Log.e("log_tag", "good connection");
                
                //display result in result activity
                Intent newInt = new Intent(EnterTextActivity.this, ResultActivity.class);
                newInt.putExtra("result", output);
                startActivity(newInt);
                
                 
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
