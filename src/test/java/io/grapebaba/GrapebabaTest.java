package io.grapebaba;

import io.grapebaba.protocol.MessageType;
import io.grapebaba.protocol.SerializerType;
import io.grapebaba.protocol.grapebaba.RequestMessage;
import io.grapebaba.protocol.grapebaba.ResponseMessage;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * A unit test for grapebaba protocol.
 */
public class GrapebabaTest {
    private static final int PORT = 8076;

    /**
     * A test for send and receive.
     */
    @Test
    public void server() {
        final int timeout = 200;
        final int opaque = 9999;
        final long waitingTime = 3L;
        TcpServer server = Grapebaba.serve(new InetSocketAddress(PORT),
                Observable.just(new TestFunc()));

        Service<RequestMessage, ResponseMessage> client =
                Grapebaba.newClient(server.getServerAddress());

        RequestMessage requestMessage =
                RequestMessage.newBuilder().withSerializerType(SerializerType.KRYO)
                        .withArguments(new Object[]{"GRAPE"})
                        .withMessageType(MessageType.REQUEST).withMethodName("call")
                        .withBeanName("TestFunc").withTimeout(timeout).withOpaque(opaque)
                        .build();

        client.call(requestMessage).subscribe(responseMessage -> {
            System.out.println(responseMessage.getResult());
        });

        server.awaitShutdown(waitingTime, TimeUnit.SECONDS);
    }

    /**
     * A function object for test.
     */
    public class TestFunc implements Func1<String, String> {

        @Override
        public String call(String s) {
            return s.toLowerCase();
        }
    }
}
