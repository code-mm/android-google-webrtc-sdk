package org.ms.webrtc;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.ms.module.supper.client.Modules;
import org.ms.webrtc.common.Event;
import org.ms.webrtc.common.InitResponse;

import java.net.URI;

public class IMWebSocketClient extends WebSocketClient {

    private static final String TAG = "IMWebSocketClient";

    //
    private IMEvent imEvent;

    // 是否连接成功
    private boolean connect = false;


    public IMWebSocketClient(URI serverUri, IMEvent event) {
        super(serverUri);
        this.imEvent = event;
    }

    public void setEvent(IMEvent event) {
        this.imEvent = event;
    }


    /**
     * 连接
     *
     * @param handshakedata
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        connect = true;
        if (imEvent != null) {
            imEvent.onOpen();
        }
    }

    /**
     * 消息接收
     *
     * @param message
     */
    @Override
    public void onMessage(String message) {


        Event event = Modules.getUtilsModule().getGsonUtils().fromJson(message, Event.class);

        String event_type = event.getEvent_type();

        if (Event.EVENT_INIT.equals(event_type)) {
            Object data = event.getData();
            InitResponse initResponse = (InitResponse) data;
            Boolean status = initResponse.getStatus();
            if (status) {
                Modules.getLogModule().d(TAG, "信息注册成功");
                if (imEvent != null) {
                    imEvent.init(true, "成功");
                }
            } else {
                Modules.getLogModule().d(TAG, "信息注册失败");
                imEvent.init(false, "失败");
            }
        } else if (Event.EVENT_INVITE.equals(event_type)) {

        } else if (Event.EVENT_CANCEL.equals(event_type)) {

        } else if (Event.EVENT_RING.equals(event_type)) {

        } else if (Event.EVENT_PEERS.equals(event_type)) {

        } else if (Event.EVENT_NEW_PEERS.equals(event_type)) {

        } else if (Event.EVENT_REJECT.equals(event_type)) {

        } else if (Event.EVENT_OFFER.equals(event_type)) {

        } else if (Event.EVENT_ANSWER.equals(event_type)) {

        } else if (Event.EVENT_ICE_CANDIDATE.equals(event_type)) {

        } else if (Event.EVENT_LEAVE.equals(event_type)) {

        } else if (Event.EVENT_AUDIO.equals(event_type)) {

        } else if (Event.EVENT_DISCONNECT.equals(event_type)) {

        }


    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        connect = false;
    }

    @Override
    public void onError(Exception ex) {
        connect = false;
        ex.printStackTrace();
    }
}
