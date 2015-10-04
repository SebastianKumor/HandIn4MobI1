package com.corporation.tvm.handin4camera;


import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.List;

/**
 * Created by sebastiankumor on 03/10/15.
 */
public class CustomSurfaceView extends SurfaceView  {

    private static final String TAG = "CameraTag";

    private SurfaceHolder holder;
    private Camera mCamera;
    List<Camera.Size> mCameraSizes;
    Camera.Size optimalSize;
  //  private Context context;

    public CustomSurfaceView(Context context) {
        super(context);


//        mCamera=getCameraInstance();


        mCameraSizes=mCamera.getParameters().getSupportedPreviewSizes();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        holder = getHolder();
      //  holder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CustomSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }
    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }








}
