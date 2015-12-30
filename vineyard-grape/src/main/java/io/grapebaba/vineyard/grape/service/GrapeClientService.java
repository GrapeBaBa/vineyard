/*
 * Copyright 2015 281165273grape@gmail.com
 *
 * Licensed under the Apache License, version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.grapebaba.vineyard.grape.service;

import io.grapebaba.vineyard.common.Service;
import io.grapebaba.vineyard.common.client.VineyardClient;
import io.grapebaba.vineyard.grape.protocol.grape.RequestMessage;
import io.grapebaba.vineyard.grape.protocol.grape.ResponseMessage;
import rx.Observable;
import rx.subjects.ReplaySubject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Grape rpc client.
 */
public class GrapeClientService implements Service<RequestMessage, ResponseMessage> {
    private final ConcurrentMap<Integer, ReplaySubject<ResponseMessage>> req2Res = new ConcurrentHashMap<>();

    private final VineyardClient<RequestMessage, ResponseMessage> client;

    /**
     * Constructor.
     *
     * @param client input
     */
    public GrapeClientService(VineyardClient<RequestMessage, ResponseMessage> client) {
        this.client = client;
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
                                    .remove(message.getOpaque());
                            responseMessageReplaySubject.onNext(responseMessage);
                            responseMessageReplaySubject.onCompleted();
                        });

        return response;
    }

}
