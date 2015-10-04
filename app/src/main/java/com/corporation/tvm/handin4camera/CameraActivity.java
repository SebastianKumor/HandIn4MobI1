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


public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

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



        mCamera =getCameraInstance();
        surfview = (SurfaceView)findViewById(R.id.surf_view);
        surfaceHolder = surfview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        setCameraDisplayOrientation(CameraActivity.this, findFrontFacingCameraID(), mCamera);
        picButton = (Button) findViewById(R.id.camera_button);

        // surfaceHolder.addCallback(CameraActivity.this);

    }







    // getting optimal size for camera

    public void refreshCamera() {

        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {

            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }


        // set preview size and make any resize, rotate or
        // reformatting changes here

//        Camera.Parameters parameters = mCamera.getParameters();
//        parameters.setPreviewSize(optimalSize.width, optimalSize.height);
//        mCamera.setParameters(parameters);
//        mCamera.startPreview();
        // start preview with new settings

        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }

    private int findFrontFacingCameraID() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public static void setCameraDisplayOrientation(CameraActivity activity,
                                                   int cameraId, Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result);
    }


    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            // open the camera
            mCamera = Camera.open();
        } catch (RuntimeException e) {

            // check for exceptions
            System.err.println(e);
            return;
        }

        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            mCamera.setPreviewDisplay(this.surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.

        mCamera.stopPreview();
        mCamera.release();

        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        refreshCamera();
    }




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
