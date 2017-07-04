package com.msfc.dapple;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    String cookie;
    Camera camera;
    CameraPreview preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Intent cookieIntent = getIntent();
        cookie=cookieIntent.getStringExtra("cookie");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
    protected void onResume(){
        super.onResume();

    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            camera = Camera.open(id);
            qOpened = (camera != null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return qOpened;
    }

    private void releaseCameraAndPreview() {
        preview.setCamera(null);
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}

