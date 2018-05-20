package com.example.jamespc.pentaxtestapp;

import android.annotation.SuppressLint;
import com.ricoh.camera.sdk.wireless.api.CameraDevice;

import android.bluetooth.BluetoothClass;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ricoh.camera.sdk.wireless.api.CameraDeviceDetector;
import com.ricoh.camera.sdk.wireless.api.DeviceInterface;
import com.ricoh.camera.sdk.wireless.api.response.Response;
import com.ricoh.camera.sdk.wireless.api.response.Result;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView deviceConnectText;
    TextView batteryLifeText;
    Button connectBtn;
    CameraDevice cameraDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceConnectText = this.findViewById(R.id.deviceConnectText);
        batteryLifeText = this.findViewById(R.id.batteryLifeText);
        connectBtn = this.findViewById(R.id.connectBtn);

        connectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                connect();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void connect() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                if (cameraDevice == null) {
                    List<CameraDevice> detectedDevices =
                            CameraDeviceDetector.detect(DeviceInterface.WLAN);
                    if (detectedDevices.isEmpty()) {
                        return "No devices were found!";
                    }
                    cameraDevice = detectedDevices.get(0);
                } else if (!cameraDevice.isConnected(DeviceInterface.WLAN)) {
                    List<CameraDevice> detectedDevices =
                            CameraDeviceDetector.detect(DeviceInterface.WLAN);
                    if (!detectedDevices.isEmpty()) {
                        cameraDevice = detectedDevices.get(0);
                    }
                }

                Response response = cameraDevice.connect(DeviceInterface.WLAN);

                if (response.getResult() == Result.OK) {
                    return "Success!";
                } else {
                    return "Failed!";
                }
            }

            protected void onPostExecute(String result) {
                deviceConnectText.setText(result);
                if (cameraDevice != null) {
                    batteryLifeText.setText("Battery Level %" + cameraDevice.getStatus().getBatteryLevel());
                }
            }
        }.execute();
    }
}
