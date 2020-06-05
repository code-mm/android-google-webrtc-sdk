package com.example.ms_webrtc_app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.ms.module.supper.client.Modules;
import org.ms.module.supper.inter.common.CallBack;
import org.webrtc.SurfaceViewRenderer;

public class MainActivity extends AppCompatActivity {


    private EditText editTextUserId;
    private EditText editTextBuddyId;

    private Button buttonConnect;
    private Button buttonInit;

    private Button buttonCall;


    private SurfaceViewRenderer localSurfaceView;
    private SurfaceViewRenderer remoteSurfaceView;

    private WebRTCClient webRTCClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Modules.getPermissionModule().request(this, new CallBack() {
            @Override
            public void onSuccess(Object o) {
            }

            @Override
            public void onFailure(Object o) {

            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        buttonConnect = findViewById(R.id.buttonConnect);
        buttonInit = findViewById(R.id.buttonInit);
        buttonCall = findViewById(R.id.buttonCall);

        editTextUserId = findViewById(R.id.editTextUserId);
        editTextBuddyId = findViewById(R.id.editTextBuddyId);


        localSurfaceView = findViewById(R.id.LocalSurfaceView);
        remoteSurfaceView = findViewById(R.id.RemoteSurfaceView);


        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                webRTCClient = WebRTCClient.Builder.builder()
                        .setContext(MainActivity.this)
                        .setUrl("ws://192.168.0.108:5000/ws")
                        .setLocalSurfaceView(localSurfaceView)
                        .setRemoteSurfaceView(remoteSurfaceView)
                        .setUserId(editTextUserId.getText().toString())
                        .build();


                webRTCClient.connect();
            }
        });

        buttonInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webRTCClient.createPeerConnection();
            }
        });


        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webRTCClient.offerVoice(editTextBuddyId.getText().toString());
            }
        });
    }
}
