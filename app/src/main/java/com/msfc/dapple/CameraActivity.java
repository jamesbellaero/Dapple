package com.msfc.dapple;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import org.w3c.dom.Text;

import java.nio.ByteBuffer;


public class CameraActivity extends AppCompatActivity implements CompletedListener,NetworkListener {
    String cookie;
    TextureView view;
    Camera camera;
    byte[] imageBytes;
    Activity activity;
    FloatingActionButton postButton, retryButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_camera);
        view = (TextureView) findViewById(R.id.cameraView);

        postButton = (FloatingActionButton) findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Networking.postImage(imageBytes,activity);
            }
        });
        retryButton = (FloatingActionButton) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.stop();
                camera=new Camera(activity,(TextureView) findViewById(R.id.cameraView));
                //Networking.postImage(imageBytes,activity);
            }
        });
    }


    public void onResume(){
        super.onResume();
        postButton.setVisibility(View.INVISIBLE);
        retryButton.setVisibility(View.INVISIBLE);
        camera=new Camera(this,view);
        camera.initCamera();
    }
    public void onPause(){
        super.onPause();
    }
    public void onBackPressed(){
        super.onBackPressed();
        camera.stop();
    }
    public void completed(boolean success){
        if(success) {
            ByteBuffer buffer = camera.getImage().getPlanes()[0].getBuffer();
            imageBytes = new byte[buffer.capacity()];
            buffer.get(imageBytes);
            postButton.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);
        }
        camera.stop();
    }
    public void onNetworkingResponse(NetworkResponse response){
        System.out.println(response.statusCode);
    }

}

