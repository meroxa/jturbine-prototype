package com.meroxa.turbine;

import com.meroxa.turbine.proto.TurbineServiceGrpc;
import io.quarkus.grpc.GrpcService;


@GrpcService
public class TurbineService extends TurbineServiceGrpc.TurbineServiceImplBase {
}
