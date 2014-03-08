package com.legalez.legalezapp;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
    
        if (resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            mImageName.setText(selectedImage.toString());
        }
    }

}
