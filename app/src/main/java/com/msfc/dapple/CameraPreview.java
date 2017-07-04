package com.msfc.dapple;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

/**
 * Created by James on 6/30/2017.
 */

public class CameraPreview extends ViewGroup implements SurfaceHolder.Callback {

    SurfaceView surfaceView;
    SurfaceHolder holder;
    Camera camera;

    CameraPreview(Context context, Camera camera) {
        super(context);
        this.camera=camera;
        surfaceView = new SurfaceView(context);
        addView(surfaceView);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        holder = surfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    @Override
    public void onLayout(boolean b,int i1,int i2, int i3, int i4){

    }
    @Override
    public void surfaceChanged(SurfaceHolder sh,int i1, int i2, int i3){
        setCamera(camera);
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder sh){

    }
    @Override
    public void surfaceCreated(SurfaceHolder sh){
        holder=sh;
    }
    public void setCamera(Camera c){
        if(c.equals(camera))
            return;
        camera=c;
        if(camera!=null){

            try {
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            camera.startPreview();
        }
    }

}
