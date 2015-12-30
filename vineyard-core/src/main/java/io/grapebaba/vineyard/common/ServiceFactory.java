package io.grapebaba.vineyard.common;

import io.grapebaba.vineyard.common.client.VineyardClient;

public interface ServiceFactory<Req, Res> {
    Service<Req, Res> create(VineyardClient<Req, Res> client);
}
