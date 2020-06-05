package com.example.ms_webrtc_app;

import android.content.Context;

import org.webrtc.SurfaceViewRenderer;

public class WebRTCClient {

    private Context context;

    private String url;

    private WebRTCWebSocketManager webRTCWebSocketManager;


    private SurfaceViewRenderer remoteSurfaceView;

    private SurfaceViewRenderer localSurfaceView;

    private String userid;


    public String getUrl() {
        return url;
    }

    public Context getContext() {
        return context;
    }

    public String getUserid() {
        return userid;
    }

    public void connect() {
        webRTCWebSocketManager.setUserid(userid);
        webRTCWebSocketManager.connect();
    }

    public void createPeerConnection() {
        webRTCWebSocketManager.setUrl(url);
        webRTCWebSocketManager.getWebRTCEngine().setContex(context);
        webRTCWebSocketManager.getWebRTCEngine().setUserId(userid);
        webRTCWebSocketManager.getWebRTCEngine().setLocalSurfaceView(localSurfaceView);
        webRTCWebSocketManager.getWebRTCEngine().setRemoteSurfaceView(remoteSurfaceView);
        webRTCWebSocketManager.getWebRTCEngine().createPeerConnection();
    }

    public void offerVoice(String receiveId) {
        webRTCWebSocketManager.getWebRTCEngine().offerVoice(receiveId);
    }


    public static class Builder {

        private WebRTCClient webRTCClient;

        public Builder() {
            webRTCClient = new WebRTCClient();
            webRTCClient.webRTCWebSocketManager = new WebRTCWebSocketManager();
        }

        public static Builder builder() {
            Builder builder = new Builder();
            return builder;
        }

        public Builder setContext(Context context) {
            webRTCClient.context = context;
            return this;
        }

        public Builder setUrl(String url) {
            webRTCClient.url = url;
            webRTCClient.webRTCWebSocketManager.setUrl(url);
            return this;
        }

        public Builder setUserId(String userid) {
            webRTCClient.userid = userid;
            return this;
        }

        public Builder setLocalSurfaceView(SurfaceViewRenderer localSurfaceView) {
            webRTCClient.localSurfaceView = localSurfaceView;
            return this;
        }

        public Builder setRemoteSurfaceView(SurfaceViewRenderer remoteSurfaceView) {
            webRTCClient.remoteSurfaceView = remoteSurfaceView;
            return this;
        }

        public WebRTCClient build() {
            return webRTCClient;
        }
    }
}
