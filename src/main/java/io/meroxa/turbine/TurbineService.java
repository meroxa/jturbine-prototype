package io.meroxa.turbine;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import io.meroxa.turbine.core.InitRequest;
import io.quarkus.grpc.GrpcService;

import static io.meroxa.turbine.core.TurbineServiceGrpc.TurbineServiceImplBase;

@GrpcService
public class TurbineService extends TurbineServiceImplBase {
    @Override
    public void init(InitRequest request, StreamObserver<Empty> responseObserver) {
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
