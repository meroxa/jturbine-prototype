package com.meroxa.turbine.function;

import io.grpc.stub.StreamObserver;
import com.meroxa.turbine.funtime.proto.FunctionGrpc;
import com.meroxa.turbine.funtime.proto.ProcessRecordRequest;
import com.meroxa.turbine.funtime.proto.ProcessRecordResponse;
import io.quarkus.grpc.GrpcService;

@GrpcService
public class FunctionService extends FunctionGrpc.FunctionImplBase {
    @Override
    public void process(ProcessRecordRequest request, StreamObserver<ProcessRecordResponse> responseObserver) {
        super.process(request, responseObserver);
    }
}
