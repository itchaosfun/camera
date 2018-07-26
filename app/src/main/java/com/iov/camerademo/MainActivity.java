package com.iov.camerademo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends Activity {

    private CameraManager frontCameraManager;
    private CameraManager backCameraManager;
    /**
     * 定义前置有关的参数
     */
    private SurfaceView frontSurfaceView;
    private SurfaceHolder frontHolder;
    private Camera mFrontCamera;
    /**
     * 定义后置有关的参数
     */
    private SurfaceView backSurfaceView;
    private SurfaceHolder backHolder;
    private Camera mBackCamera;

    /**
     * 自动对焦的回调方法，用来处理对焦成功/不成功后的事件
     */
    private AutoFocusCallback mAutoFocus = new AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //TODO:空实现
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpUtils.getInstance().init(this);

        requestPermissions();

        String string = SpUtils.getInstance().getSp("spname").getString("init");
        if (string.equals("inited")) {
            initView();
        }
    }


    public void buttonListener(View view) {
        switch (view.getId()) {
            case R.id.openFront_button:
                takeFrontPhoto();
                break;
            case R.id.openBack_button:
                takeBackPhoto();
                break;

            default:
                break;
        }
    }

    private void initView() {
        /**
         * 初始化前置相机参数
         */
        // 初始化surface view
        frontSurfaceView = (SurfaceView) findViewById(R.id.front_surfaceview);
        // 初始化surface holder
        frontHolder = frontSurfaceView.getHolder();
        frontCameraManager = new CameraManager(mFrontCamera, frontHolder);
        /**
         * 初始化后置相机参数
         */
        // 初始化surface view
        backSurfaceView = (SurfaceView) findViewById(R.id.back_surfaceview);
        // 初始化surface holder
        backHolder = backSurfaceView.getHolder();
        backCameraManager = new CameraManager(mBackCamera, backHolder);
    }

    /**
     * @return 开启前置摄像头照相
     */
    private void takeFrontPhoto() {
        if (frontCameraManager.openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
            mFrontCamera = frontCameraManager.getCamera();
            //自动对焦
            mFrontCamera.autoFocus(mAutoFocus);
            // 拍照
            mFrontCamera.takePicture(null, null, frontCameraManager.new PicCallback(mFrontCamera));
        }
    }

    /**
     * @return 开启后置摄像头照相
     */
    private void takeBackPhoto() {
        if (backCameraManager.openCamera(Camera.CameraInfo.CAMERA_FACING_BACK)) {
            mBackCamera = backCameraManager.getCamera();
            //自动对焦
            mBackCamera.autoFocus(mAutoFocus);
            // 拍照
            mBackCamera.takePicture(null, null, backCameraManager.new PicCallback(mBackCamera));
        }
    }

    private void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA}, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initView();
        SpUtils.getInstance().getSp("spname").saveString("init", "inited");
    }
}
