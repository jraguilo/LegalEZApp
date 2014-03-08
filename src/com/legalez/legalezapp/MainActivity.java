package com.legalez.legalezapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button enterTextButton = (Button) findViewById(R.id.enter_text_button);
        enterTextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent newInt = new Intent(MainActivity.this, EnterTextActivity.class);
                startActivity(newInt);
            }
        });
        
        final Button uploadImageButton = (Button) findViewById(R.id.upload_image_button);
        uploadImageButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent newInt = new Intent(MainActivity.this, UploadImageActivity.class);
                startActivity(newInt);
            }
        });
        
        final Button cameraImageButton = (Button) findViewById(R.id.camera_button);
        cameraImageButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent newInt = new Intent(MainActivity.this, CameraImageActivity.class);
                startActivity(newInt);
            }
        });
    }


}
