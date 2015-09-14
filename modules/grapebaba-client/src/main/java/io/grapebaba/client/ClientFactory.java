package io.grapebaba.client;

import io.grapebaba.client.config.ClientConfiguration;
import io.reactivex.netty.channel.pool.PooledConnectionProvider;
import io.reactivex.netty.protocol.tcp.client.TcpClient;

public class ClientFactory {
	public static TcpClient newClient(
			ClientConfiguration clientConfiguration) {
		return TcpClient.newClient(PooledConnectionProvider.forHost("localhost", 8076))
				.pipelineConfigurator(entries -> {

				});
	}

}
