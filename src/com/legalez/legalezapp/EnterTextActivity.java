package com.legalez.legalezapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
