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

package io.grapebaba;

import com.esotericsoftware.kryo.KryoException;
import io.grapebaba.core.VineyardServer;
import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.SerializerType;
import io.grapebaba.protocol.vineyard.RequestMessage;
import io.grapebaba.protocol.vineyard.ResponseMessage;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Function;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * A unit test for vineyard protocol.
 */
public class VineyardTest {
    /**
     * A test for send and receive.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void normal() {
        final int port = 8076;
        final int timeout = 200;
        final int opaque = 9999;
        final long waitingTime = 3L;
        VineyardServer server = Vineyard.serve(new InetSocketAddress(port),
                Observable.just(new TestFunc()));

        Service<RequestMessage, ResponseMessage> client =
                Vineyard.newClient(server.getServerAddress());

        RequestMessage requestMessage =
                RequestMessage.newBuilder().withSerializerType(SerializerType.JAVA)
                        .withArguments(new Object[]{"GRAPE"})
                        .withMessageType(MessageType.REQUEST).withMethodName("call")
                        .withBeanName("io.grapebaba.VineyardTest$TestFunc").withTimeout(timeout).withOpaque(opaque)
                        .build();

        client.call(requestMessage).subscribe(responseMessage -> {
            System.out.println(responseMessage.getResult());
        });

        server.awaitShutdown(waitingTime, TimeUnit.SECONDS);
    }

    /**
     * A test for test exception.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testException() {
        final int port = 8077;
        final int timeout = 200;
        final int opaque = 9999;
        final long waitingTime = 3L;
        VineyardServer server = Vineyard.serve(new InetSocketAddress(port),
                Observable.just(new TestThrowable()));

        Service<RequestMessage, ResponseMessage> client =
                Vineyard.newClient(server.getServerAddress());

        RequestMessage requestMessage =
                RequestMessage.newBuilder().withSerializerType(SerializerType.JAVA)
                        .withArguments(new Object[]{"GRAPE"})
                        .withMessageType(MessageType.REQUEST).withMethodName("testException")
                        .withBeanName("io.grapebaba.VineyardTest$TestThrowable").withTimeout(timeout).withOpaque(opaque)
                        .build();

        client.call(requestMessage).subscribe(responseMessage -> {
            System.out.println(((InvokeError) responseMessage.getResult()).getMsg());
        });

        server.awaitShutdown(waitingTime, TimeUnit.SECONDS);
    }

    /**
     * A test for test void function.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testVoidFunction() {
        final int port = 8078;
        final int timeout = 200;
        final int opaque = 9999;
        final long waitingTime = 3L;
        VineyardServer server = Vineyard.serve(new InetSocketAddress(port),
                Observable.just(new TestVoidFunction()));

        Service<RequestMessage, ResponseMessage> client =
                Vineyard.newClient(server.getServerAddress());

        RequestMessage requestMessage =
                RequestMessage.newBuilder().withSerializerType(SerializerType.JAVA)
                        .withArguments(new Object[]{"GRAPE"})
                        .withMessageType(MessageType.REQUEST).withMethodName("testVoidFunction")
                        .withBeanName("io.grapebaba.VineyardTest$TestVoidFunction")
                        .withTimeout(timeout).withOpaque(opaque)
                        .build();

        client.call(requestMessage).subscribe(responseMessage -> {
            System.out.println(responseMessage.getResult());
            System.out.println(responseMessage.getOpaque());
        });

        server.awaitShutdown(waitingTime, TimeUnit.SECONDS);
    }

    /**
     * A function object for test.
     */
    public class TestFunc implements Func1<String, String> {

        @Override
        public String call(String source) {
            return source.toLowerCase();
        }
    }

    /**
     * A function object for test exception.
     */
    public class TestThrowable implements Function {

        /**
         * A test method for throwing exception.
         *
         * @param source input
         * @return output
         */
        public String testException(String source) {
            throw new KryoException("exception", new NullPointerException("null point"));
        }
    }

    /**
     * A function object for test void function.
     */
    public class TestVoidFunction implements Function {

        /**
         * A test method for void function.
         *
         * @param source input
         */
        public void testVoidFunction(String source) {

        }
    }
}
