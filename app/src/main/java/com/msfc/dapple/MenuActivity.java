package com.msfc.dapple;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import static com.msfc.dapple.R.id.fab;

public class MenuActivity extends AppCompatActivity {
    String cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent cookieIntent = getIntent();
        cookie=cookieIntent.getStringExtra("cookie");
        setContentView(R.layout.activity_menu);
        Button takePictures = (Button)findViewById(R.id.take_pictures_button);
        takePictures.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(),CameraActivity.class);
                intent.putExtra("cookie",cookie);
                startActivity(intent);
            }
        } );
        Button viewPictures = (Button)findViewById(R.id.view_pictures_button);
        viewPictures.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(),ViewPicAcitivity.class);
                intent.putExtra("cookie",cookie);
                startActivity(intent);
            }
        } );
    }

}
