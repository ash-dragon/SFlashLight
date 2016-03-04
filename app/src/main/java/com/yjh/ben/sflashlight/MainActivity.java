package com.yjh.ben.sflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private boolean mOn;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.btn_flash_light);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOn = !mOn;
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    if (mOn) {
                        mCamera = Camera.open();
                        mParameters = mCamera.getParameters();
                        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(mParameters);
                        mCamera.startPreview();
                    } else {
                        mCamera.stopPreview();
                        mCamera.release();
                        mCamera = null;
                    }

                    mButton.setText(mOn ? R.string.button_on : R.string.button_off);
                    findViewById(R.id.layout).setBackgroundResource(mOn ? R.color.bg_on_color : R.color.bg_off_color);
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage(R.string.no_flash_light_message)
                            .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            }
        });

        mButton.performClick();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
