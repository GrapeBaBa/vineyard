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

import static com.google.common.base.Throwables.getRootCause;
import static rx.Observable.just;

public class GrapeServerService implements Service<RequestMessage,ResponseMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrapeServerService.class);

    private final Observable<Function> functionObservable;

    public GrapeServerService(Observable<Function> functionObservable){
        this.functionObservable=functionObservable;
    }

    @Override
    public Observable<ResponseMessage> call(RequestMessage requestMessage) {
        final String beanName = requestMessage.getBeanName();

        return functionObservable
                .first(function -> function.getClass()
                        .getName().equals(beanName))
                .flatMap(
                        function -> {
                            Object result;
                            try {
                                result = MethodUtils
                                        .invokeMethod(
                                                function,
                                                requestMessage
                                                        .getMethodName(),
                                                requestMessage
                                                        .getArguments());
                            } catch (Throwable e) {
                                LOGGER.error(
                                        "Invoke service method exception",
                                        e);
                                Throwable root = getRootCause(e);
                                result = new ErrorResponse(
                                        null == root
                                                ? "Cannot find root cause exception"
                                                : root.getClass().getName()
                                                + ":" + root.getMessage());
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
                        });
    }
}
