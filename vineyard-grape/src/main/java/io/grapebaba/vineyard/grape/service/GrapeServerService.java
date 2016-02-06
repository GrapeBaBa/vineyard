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

import io.grapebaba.vineyard.grape.ErrorResponse;
import io.grapebaba.vineyard.common.Service;
import io.grapebaba.vineyard.grape.protocol.MessageType;
import io.grapebaba.vineyard.grape.protocol.grape.RequestMessage;
import io.grapebaba.vineyard.grape.protocol.grape.ResponseMessage;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Function;

import java.lang.reflect.InvocationTargetException;

import static com.google.common.base.Throwables.getRootCause;
import static com.google.common.base.Throwables.propagate;
import static rx.Observable.just;

/**
 * Grape rpc server.
 */
public class GrapeServerService implements Service<RequestMessage, ResponseMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrapeServerService.class);

    private final Observable<Function> functionObservable;

    /**
     * Constructor.
     *
     * @param functionObservable input
     */
    public GrapeServerService(Observable<Function> functionObservable) {
        this.functionObservable = functionObservable;
    }

    @Override
    public Observable<ResponseMessage> call(RequestMessage requestMessage) {
        final String beanName = requestMessage.getBeanName();

        return functionObservable
                .first(function -> function.getClass()
                        .getName().equals(beanName))
                .flatMap(
                        function -> {
                            Object result = null;
                            try {
                                result = MethodUtils
                                        .invokeMethod(
                                                function,
                                                requestMessage
                                                        .getMethodName(),
                                                requestMessage
                                                        .getArguments());
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                propagate(e);
                            }

                            ResponseMessage responseMessage = ResponseMessage
                                    .newBuilder()
                                    .withMessageType(MessageType.RESPONSE)
                                    .withSerializerType(requestMessage
                                            .getSerializerType())
                                    .withOpaque(requestMessage.getOpaque())
                                    .withResult(result)
                                    .build();
                            return just(responseMessage);

                        }).onErrorReturn(throwable -> {
                            LOGGER.error(
                                    "Invoke service method exception",
                                    throwable);
                            Throwable root = getRootCause(throwable);
                            Object result = new ErrorResponse(
                                    null == root
                                            ? "Cannot find root cause exception"
                                            : root.getClass().getName()
                                            + ":" + root.getMessage());

                            ResponseMessage responseMessage = ResponseMessage
                                    .newBuilder()
                                    .withMessageType(MessageType.RESPONSE)
                                    .withSerializerType(requestMessage
                                            .getSerializerType())
                                    .withOpaque(requestMessage.getOpaque())
                                    .withResult(result)
                                    .build();

                            return responseMessage;
                        });
    }
}
