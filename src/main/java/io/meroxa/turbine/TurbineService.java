package io.meroxa.turbine;

import io.meroxa.turbine.proto.TurbineServiceGrpc;
import io.quarkus.grpc.GrpcService;


@GrpcService
public class TurbineService extends TurbineServiceGrpc.TurbineServiceImplBase {
}
