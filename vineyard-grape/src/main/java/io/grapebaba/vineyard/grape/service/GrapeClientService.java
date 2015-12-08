package io.grapebaba.vineyard.grape.service;

import io.grapebaba.vineyard.common.Service;
import io.grapebaba.vineyard.common.client.VineyardClient;
import io.grapebaba.vineyard.common.codec.packet.PacketDecoder;
import io.grapebaba.vineyard.common.codec.packet.PacketEncoder;
import io.grapebaba.vineyard.grape.codec.grape.GrapeCodecAdapter;
import io.grapebaba.vineyard.grape.protocol.grape.RequestMessage;
import io.grapebaba.vineyard.grape.protocol.grape.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.tcp.client.TcpClient;
import netflix.ocelli.rxnetty.protocol.tcp.TcpLoadBalancer;
import rx.Observable;
import rx.subjects.ReplaySubject;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static netflix.ocelli.Instance.create;
import static rx.Observable.*;

public class GrapeClientService implements Service<RequestMessage,ResponseMessage> {

    private final ConcurrentMap<Integer, ReplaySubject<ResponseMessage>> req2Res = new ConcurrentHashMap<>();

    private final VineyardClient<RequestMessage, ResponseMessage> client;

    public GrapeClientService(VineyardClient<RequestMessage,ResponseMessage> client) {
        this.client=client;
    }

    @Override
    public Observable<ResponseMessage> call(RequestMessage message) {
        ReplaySubject<ResponseMessage> response = ReplaySubject.create();
        req2Res.putIfAbsent(message.getOpaque(), response);

        client.createConnectionRequest()
                .flatMap(
                        connection -> connection.write(Observable.just(message))
                                .cast(ResponseMessage.class)
                                .mergeWith(connection.getInput()))
                .subscribe(
                        responseMessage -> {
                            ReplaySubject<ResponseMessage> responseMessageReplaySubject = req2Res
                                    .get(message.getOpaque());
                            responseMessageReplaySubject.onNext(responseMessage);
                            responseMessageReplaySubject.onCompleted();
                        });

        return response;
    }

}