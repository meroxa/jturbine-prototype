package io.meroxa.turbine.local;

import io.grpc.ManagedChannelBuilder;
import io.meroxa.turbine.proto.TurbineServiceGrpc;

public class TurbineCoreClient {
    private final TurbineServiceGrpc.TurbineServiceBlockingStub stub;

    public TurbineCoreClient(String turbineCoreAddress) {
        String[] split = turbineCoreAddress.split(":");
        var channelBuilder = ManagedChannelBuilder.forAddress(split[0], Integer.parseInt(split[1])).usePlaintext();
        var channel = channelBuilder.build();
        stub = TurbineServiceGrpc.newBlockingStub(channel);
    }
}
