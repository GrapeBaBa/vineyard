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
import io.grapebaba.vineyard.grape.ErrorResponse;
import io.grapebaba.vineyard.common.Service;
import io.grapebaba.vineyard.common.serializer.SerializerType;
import io.grapebaba.vineyard.grape.Grape;
import io.grapebaba.vineyard.grape.protocol.MessageType;
import io.grapebaba.vineyard.grape.protocol.grape.RequestMessage;
import io.grapebaba.vineyard.grape.protocol.grape.ResponseMessage;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Function;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static io.grapebaba.vineyard.common.Opaque.next;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * A unit test for vineyard protocol.
 */
public class GrapeTest {
    /**
     * A test for send and receive.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void normal() throws Exception {
        final int port = 8076;
        final int timeout = 200;
        final int opaque = next();
        final long waitingTime = 30L;
        final AtomicReference<Object> result = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        TcpServer server = Grape.serve(new InetSocketAddress(port),
                Observable.just(new TestFunc()));

        Service<RequestMessage, ResponseMessage> client =
                Grape.newClient(server.getServerAddress());

        RequestMessage requestMessage =
                RequestMessage.newBuilder().withSerializerType(SerializerType.JAVA)
                        .withArguments(new Object[]{"GRAPE"})
                        .withMessageType(MessageType.REQUEST).withMethodName("call")
                        .withBeanName("io.grapebaba.GrapeTest$TestFunc").withTimeout(timeout).withOpaque(opaque)
                        .build();

        client.call(requestMessage).subscribe(responseMessage -> {
            result.set(responseMessage.getResult());
            latch.countDown();
        });

        assertTrue(latch.await(waitingTime, TimeUnit.SECONDS));
        assertEquals("grape", result.get());
    }

    /**
     * A test for test exception.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testException() throws Exception {
        final int port = 8077;
        final int timeout = 200;
        final int opaque = next();
        final long waitingTime = 30L;
        final AtomicReference<Object> result = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        TcpServer server = Grape.serve(new InetSocketAddress(port),
                Observable.just(new TestThrowable()));

        Service<RequestMessage, ResponseMessage> client =
                Grape.newClient(server.getServerAddress());

        RequestMessage requestMessage =
                RequestMessage.newBuilder().withSerializerType(SerializerType.JAVA)
                        .withArguments(new Object[]{"GRAPE"})
                        .withMessageType(MessageType.REQUEST).withMethodName("testException")
                        .withBeanName("io.grapebaba.GrapeTest$TestThrowable").withTimeout(timeout).withOpaque(opaque)
                        .build();

        client.call(requestMessage).subscribe(responseMessage -> {
            result.set(responseMessage.getResult());
            latch.countDown();
        });

        assertTrue(latch.await(waitingTime, TimeUnit.SECONDS));
        assertTrue(result.get() instanceof ErrorResponse);
    }

    /**
     * A test for test void function.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testVoidFunction() throws Exception {
        final int port = 8078;
        final int timeout = 200;
        final int opaque = next();
        final long waitingTime = 30L;
        final AtomicReference<Object> result = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);
        TcpServer server = Grape.serve(new InetSocketAddress(port),
                Observable.just(new TestVoidFunction()));

        Service<RequestMessage, ResponseMessage> client =
                Grape.newClient(server.getServerAddress());

        RequestMessage requestMessage =
                RequestMessage.newBuilder().withSerializerType(SerializerType.JAVA)
                        .withArguments(new Object[]{"GRAPE"})
                        .withMessageType(MessageType.REQUEST).withMethodName("testVoidFunction")
                        .withBeanName("io.grapebaba.GrapeTest$TestVoidFunction")
                        .withTimeout(timeout).withOpaque(opaque)
                        .build();

        client.call(requestMessage).subscribe(responseMessage -> {
            result.set(responseMessage);
            latch.countDown();
        });

        assertTrue(latch.await(waitingTime, TimeUnit.SECONDS));
        assertNull(((ResponseMessage) result.get()).getResult());
        assertEquals(new Integer(opaque), ((ResponseMessage) result.get()).getOpaque());
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
