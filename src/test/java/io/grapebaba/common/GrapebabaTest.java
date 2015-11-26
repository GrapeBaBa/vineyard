package io.grapebaba.common;

import io.grapebaba.common.protocol.MessageType;
import io.grapebaba.common.protocol.SerializerType;
import io.grapebaba.common.protocol.grapebaba.RequestMessage;
import io.grapebaba.common.protocol.grapebaba.ResponseMessage;
import io.reactivex.netty.protocol.tcp.server.TcpServer;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;

import java.net.InetSocketAddress;

public class GrapebabaTest {

	@Test
	public void server() {
		TcpServer server = Grapebaba.serve(new InetSocketAddress(8076),
				Observable.just(new TestFunc()));

		Service<RequestMessage, ResponseMessage> client = Grapebaba.newClient(server
				.getServerAddress());

		RequestMessage requestMessage = RequestMessage.newBuilder()
				.withSerializerType(SerializerType.KRYO)
				.withArguments(new Object[] { "GRAPE" })
				.withMessageType(MessageType.REQUEST).withMethodName("call")
				.withBeanName("TestFunc").withTimeout(200).withOpaque(9999).build();

		client.call(requestMessage).subscribe(responseMessage -> {
			System.out.println(responseMessage.getResult());
		});

		server.awaitShutdown();
	}

	public class TestFunc implements Func1<String, String> {

		@Override
		public String call(String s) {
			return s.toLowerCase();
		}
	}
}
