package com.corporation.tvm.handin4camera;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.view.View;
import android.widget.FrameLayout;


import java.util.List;


public class CameraActivity extends AppCompatActivity  {

    private static final String TAG = "CameraInfoTag";



    private Camera mCamera;
    private SurfaceView surfview;
    private SurfaceHolder surfaceHolder;
    private Button picButton;

    List<Camera.Size> mCameraSizes;
    Camera.Size optimalSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_camera);



        FrameLayout frame =(FrameLayout)findViewById(R.id.camera_activity_frame);
                frame.addView(new CustomSurfaceView(this));
        // Create an instance of Camera

     //   mCamera =getCameraInstance();
//        surfview = (SurfaceView)findViewById(R.id.surf_view);
//        surfaceHolder = surfview.getHolder();
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        picButton=(Button)findViewById(R.id.camera_button);

       // surfaceHolder.addCallback(CameraActivity.this);

       // mCameraSizes=mCamera.getParameters().getSupportedPreviewSizes();
    }





    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public static CameraActivity getActivity(){


        return getActivity();
    }


    // getting optimal size for camera



//    public void refreshCamera() {
//
//        if (surfaceHolder.getSurface() == null) {
//            // preview surface does not exist
//            return;
//        }
//
//        // stop preview before making changes
//        try {
//
//            mCamera.stopPreview();
//        } catch (Exception e) {
//            // ignore: tried to stop a non-existent preview
//        }
//
//        setCameraDisplayOrientation(CameraActivity.this, findFrontFacingCameraID(), mCamera);
//        // set preview size and make any resize, rotate or
//        // reformatting changes here
//
//        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.setPreviewSize(optimalSize.width, optimalSize.height);
//        mCamera.setParameters(parameters);
//        mCamera.startPreview();
//        // start preview with new settings
//
//        try {
//            mCamera.setPreviewDisplay(surfaceHolder);
//            mCamera.startPreview();
//        } catch (Exception e) {
//
//        }
//    }
//
//
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                               int height) {
//        refreshCamera();
//
//
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        // TODO Auto-generated method stub
//
//        try {
//            // open the camera
//            mCamera = Camera.open();
//        } catch (RuntimeException e) {
//
//            // check for exceptions
//            System.err.println(e);
//            return;
//        }
//
//        try {
//            // The Surface has been created, now tell the camera where to draw
//            // the preview.
//            mCamera.setPreviewDisplay(surfaceHolder);
//            mCamera.startPreview();
//        } catch (Exception e) {
//            // check for exceptions
//            System.err.println(e);
//            return;
//        }
//
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        // TODO Auto-generated method stub
//
//        mCamera.stopPreview();
//        mCamera.release();
//
//        mCamera = null;
//
//    }


}
