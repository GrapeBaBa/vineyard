package io.grapebaba;

public interface ServiceFactory<Req,Res> {
    Service<Req,Res> create();
}
