package com.msfc.dapple;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jtb20 on 7/7/2017.
 */

public class Camera {

    CameraManager manager;
    CameraDevice camera;
    Activity activity;
    List<Surface> cameraSurfaces;
    CameraCaptureSession captureSession;
    TextureView view;
    ImageReader imageReader;
    Image image;
    CaptureRequest.Builder captureBuilder;
    private final TextureView.SurfaceTextureListener surfaceTextureListener
            = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            startCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            startCamera(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }
    };

    public Camera(Activity activity,TextureView view){
        this.activity=activity;
        this.view=view;
        cameraSurfaces = new ArrayList<>();
        view.setSurfaceTextureListener(surfaceTextureListener);

    }
    private boolean hasCamera(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    public void initCamera(){
        if(!hasCamera(activity)){
            Toast t = Toast.makeText(activity,"Failed to find camera",Toast.LENGTH_SHORT);
            t.show();
        }
        manager= (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];
            int permissionCheck = ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.CAMERA);//lolololololol
            if(permissionCheck!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
            }

            manager.openCamera(cameraId,new CameraDevice.StateCallback(){
                public void onClosed(CameraDevice cam){
                    super.onClosed(cam);
                }
                public void onDisconnected(CameraDevice cam){
                    System.out.println("Camera disconnected");
                }
                public void onError(CameraDevice cam, int error){
                    System.out.println("lol an error with your camera");
                }
                public void onOpened(CameraDevice cam){
                    System.out.println("Camera opened");
                    camera=cam;
                }
            },null);
        }catch(CameraAccessException e){e.printStackTrace();}
    }

    public void startCamera(int width, int height){
        imageReader=ImageReader.newInstance(width,height,ImageFormat.JPEG,3);//view.getWidth(),view.getHeight(), ImageFormat.JPEG,3);
        imageReader.setOnImageAvailableListener(new OnImageAvailableListener(),null);
        try{
            SurfaceTexture texture = view.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            //texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.

            captureBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(surface);
            captureBuilder.addTarget(imageReader.getSurface());
            cameraSurfaces.add(surface);
            cameraSurfaces.add(imageReader.getSurface());

            // Here, we create a CameraCaptureSession for camera preview.
            camera.createCaptureSession(cameraSurfaces,
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            System.out.println("Capture session created");
                            captureSession=session;
                            try {
                                session.capture(captureBuilder.build(), new CaptureCallback(), null);
                            }catch(CameraAccessException e){e.printStackTrace();}
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            System.out.println("Couldn't create capture session");
                            captureSession=session;
                        }
                    },null);

        }catch(CameraAccessException e){e.printStackTrace();}
    }
    public Image getImage(){
        return image;
    }
    public void stop(){
        if(image!=null)
            image.close();
        if(imageReader!=null)
            imageReader.close();
        if(captureSession!=null)
            captureSession.close();
        if(camera!=null)
            camera.close();
    }
    class OnImageAvailableListener implements ImageReader.OnImageAvailableListener{
        public void onImageAvailable(ImageReader reader) {
            try {
                image = reader.acquireLatestImage();
                ((CompletedListener)activity).completed(true);
            } catch (Exception e) {
                ((CompletedListener)activity).completed(false);
                e.printStackTrace();
            }
        }
    }
    class CaptureCallback extends CameraCaptureSession.CaptureCallback{
        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timeStamp,
                                     long frameNumber){
            System.out.println("Capture started");
        }
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result){
            System.out.println("Capture started");
        }
        @Override
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure){
            System.out.println("Capture started");
        }
    }
}
