package org.ms.webrtc;

import org.webrtc.CandidatePairChangeEvent;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpTransceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;

import java.util.List;

public class Peer implements SdpObserver, PeerConnection.Observer {


    private PeerConnection peerConnection;
    private List<PeerConnection.IceServer> iceServerList;
    private PeerConnectionFactory peerConnectionFactory;
    private List<IceCandidate> queuedRemoteCandidates;
    public MediaStream remoteMediaStream;
    public SurfaceViewRenderer surfaceViewRenderer;
    public ProxyVideoSink proxyVideoSink;

    public Peer() {

        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServerList);
        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, this);
    }


    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {

    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

    }

    @Override
    public void onStandardizedIceConnectionChange(PeerConnection.IceConnectionState newState) {

    }

    @Override
    public void onConnectionChange(PeerConnection.PeerConnectionState newState) {

    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {

    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {

    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

    }

    @Override
    public void onSelectedCandidatePairChanged(CandidatePairChangeEvent event) {

    }

    @Override
    public void onAddStream(MediaStream mediaStream) {

    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {

    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {

    }

    @Override
    public void onRenegotiationNeeded() {

    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {

    }

    @Override
    public void onTrack(RtpTransceiver transceiver) {

    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {

    }

    @Override
    public void onSetSuccess() {

    }

    @Override
    public void onCreateFailure(String s) {

    }

    @Override
    public void onSetFailure(String s) {

    }
}
