package com.example.ms_webrtc_app;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.ms.module.supper.client.Modules;
import org.ms.utils.supper.SupperUtilsClient;
import org.ms.webrtc.common.Device;
import org.ms.webrtc.common.Event;
import org.ms.webrtc.common.IceCandidate;
import org.ms.webrtc.common.SessionDescription;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class WebRTCWebSocketManager {
    // 初始化状态
    private boolean initStatus = false;

    private WebRTCEngine webRTCEngine;

    private String url;

    private WebRTCWebSocketClient webRTCWebSocketClient;


    private String userid;


    public void setUserid(String userid) {
        this.userid = userid;
    }

    public WebRTCWebSocketManager() {

    }


    public WebRTCEngine getWebRTCEngine() {
        return webRTCEngine;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 初始化状态
    public boolean isInitStatus() {
        return initStatus;
    }

    public void connect() {
        try {
            webRTCWebSocketClient = new WebRTCWebSocketClient(new URI(url));
            webRTCWebSocketClient.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public void reconnect() {

        if (webRTCWebSocketClient != null) {
            if (!webRTCWebSocketClient.isOpen()) {
                webRTCWebSocketClient.connect();
            }
        }

    }

    public boolean isOpen() {
        if (webRTCWebSocketClient != null) {
            return webRTCWebSocketClient.isOpen();
        }
        return false;
    }


    // 初始化交换基本信息
    public void init() {
        if (isOpen()) {
            Event initEvent = Event.builder().event_type(Event.EVENT_INIT_REQUEST)
                    .device(Device.builder().name("aa").type("android").build())

                    .sender_user_id(userid).build();
            webRTCWebSocketClient.send(initEvent.toJson());

        } else {
            reconnect();
        }
    }


    class WebRTCWebSocketClient extends WebSocketClient {


        private static final String TAG = "WebRTCWebSocketClient";

        public WebRTCWebSocketClient(URI serverUri) {
            super(serverUri);

        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.e(TAG, "onOpen: 连接成功");
            webRTCEngine = new WebRTCEngine(webRTCWebSocketClient);
            init();
        }

        @Override
        public void onMessage(String message) {
            Log.e(TAG, "onMessage: " + message);
            Event event = SupperUtilsClient.getUtils().getGsonUtils().fromJson(message, Event.class);

            String event_type = event.getEvent_type();
            // 初始化交换基本信息
            if (Event.EVENT_INIT_RESPONSE.equals(event_type)) {

                boolean registered_result = event.isRegistered_result();

                if (registered_result) {

                    Log.e(TAG, "onMessage: 初始化成功");
                } else {

                    Log.e(TAG, "onMessage: 初始化失败");
                }
                //
            } else if (Event.EVENT_OFFER.equals(event_type)) {


                // 这个id为自己的id
                String receive_user_id = event.getReceive_user_id();
                // 这个id为发送者的id
                String send_user_id = event.getSender_user_id();
                // 获取sdp
                SessionDescription sessionDescription = event.getSessionDescription();
                // 通话类型
                String type = event.getCall_type();

                Log.e(TAG, "onMessage: 发送者id : " + send_user_id);
                Log.e(TAG, "onMessage: 接收者id : " + receive_user_id);
                Log.e(TAG, "onMessage: 请求通话类型 : " + type);
                Log.e(TAG, "onMessage: sdp类型 : " + sessionDescription.getType());
                Log.e(TAG, "onMessage: sdp:\n" + sessionDescription.getDescription());

                org.webrtc.SessionDescription sdp = null;

                if (org.webrtc.SessionDescription.Type.OFFER.name().equals(sessionDescription.getType())) {

                    sdp = new org.webrtc.SessionDescription(org.webrtc.SessionDescription.Type.OFFER, sessionDescription.getDescription());
                } else if (org.webrtc.SessionDescription.Type.ANSWER.name().equals(sessionDescription.getType())) {
                    sdp = new org.webrtc.SessionDescription(org.webrtc.SessionDescription.Type.ANSWER, sessionDescription.getDescription());
                } else if (org.webrtc.SessionDescription.Type.PRANSWER.name().equals(sessionDescription.getType())) {
                    sdp = new org.webrtc.SessionDescription(org.webrtc.SessionDescription.Type.PRANSWER, sessionDescription.getDescription());
                } else {
                    Log.e(TAG, "onMessage: 没有找到对应类型");
                }

                webRTCEngine.createAnswer(send_user_id, sdp);

                // 应答视频通话邀请
            } else if (Event.EVENT_ANSWER.equals(event_type)) {
                String sender_user_id = event.getSender_user_id();
                SessionDescription sessionDescription = event.getSessionDescription();
                org.webrtc.SessionDescription sdp = null;
                if (org.webrtc.SessionDescription.Type.OFFER.name().equals(sessionDescription.getType())) {
                    sdp = new org.webrtc.SessionDescription(org.webrtc.SessionDescription.Type.OFFER, sessionDescription.getDescription());
                } else if (org.webrtc.SessionDescription.Type.ANSWER.name().equals(sessionDescription.getType())) {
                    sdp = new org.webrtc.SessionDescription(org.webrtc.SessionDescription.Type.ANSWER, sessionDescription.getDescription());
                } else if (org.webrtc.SessionDescription.Type.PRANSWER.name().equals(sessionDescription.getType())) {
                    sdp = new org.webrtc.SessionDescription(org.webrtc.SessionDescription.Type.PRANSWER, sessionDescription.getDescription());
                } else {
                    Log.e(TAG, "onMessage: 没有找到对应类型");
                }
                webRTCEngine.createOffer(sender_user_id, sdp);

            } else if (Event.EVENT_ICE.equals(event_type)) {

                IceCandidate iceCandidate = event.getIceCandidate();
                org.webrtc.IceCandidate ice = new org.webrtc.IceCandidate(iceCandidate.getSdpMid(), iceCandidate.getSdpMLineIndex(), iceCandidate.getSdp());
                webRTCEngine.addIceCandidate(ice);
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {

            Log.e(TAG, "onClose:  code : " + code + " reason : " + reason + " remote : " + remote);
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }
    }

}
