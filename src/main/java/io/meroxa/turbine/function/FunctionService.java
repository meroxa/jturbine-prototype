package io.meroxa.turbine.function;

import io.grpc.stub.StreamObserver;
import io.meroxa.turbine.funtime.proto.FunctionGrpc;
import io.meroxa.turbine.funtime.proto.ProcessRecordRequest;
import io.meroxa.turbine.funtime.proto.ProcessRecordResponse;
import io.quarkus.grpc.GrpcService;

@GrpcService
public class FunctionService extends FunctionGrpc.FunctionImplBase {
    @Override
    public void process(ProcessRecordRequest request, StreamObserver<ProcessRecordResponse> responseObserver) {
        super.process(request, responseObserver);
    }
}
