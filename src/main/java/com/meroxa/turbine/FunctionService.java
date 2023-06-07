package com.meroxa.turbine;

import com.google.protobuf.Timestamp;
import com.meroxa.turbine.deploy.DeployTurbine;
import com.meroxa.turbine.funtime.proto.FunctionGrpc;
import com.meroxa.turbine.funtime.proto.ProcessRecordRequest;
import com.meroxa.turbine.funtime.proto.ProcessRecordResponse;
import com.meroxa.turbine.funtime.proto.Record;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class FunctionService extends FunctionGrpc.FunctionImplBase {
    @Inject
    Instance<DeployTurbine> turbine;

    @Override
    public void process(ProcessRecordRequest request,
                        StreamObserver<ProcessRecordResponse> responseObserver) {

    }

    private List<TurbineRecord> toTurbineRecords(List<Record> protoRecords) {
        return Utils.toStream(protoRecords)
            .map(this::toTurbineRecord)
            .collect(Collectors.toList());
    }

    private TurbineRecord toTurbineRecord(Record record) {
        return TurbineRecord.builder()
            .key(record.getKey())
            .payload(record.getValue().toString())
            .timestamp(toJavaTimestamp(record.getTimestamp()))
            .build();
    }

    private LocalDateTime toJavaTimestamp(long timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
    }

    private com.meroxa.turbine.funtime.proto.Record toProtoRecord(TurbineRecord record) {
        return com.meroxa.turbine.funtime.proto.Record
            .newBuilder()
            .setKey(record.getKey())
            .setValue(record.getPayload())
            .setTimestamp(record.getTimestamp().getSecond())
            .build();
    }

    private Timestamp toProtoTimestamp(LocalDateTime timestamp) {
        return Timestamp.newBuilder()
            .setSeconds(timestamp.getSecond())
            .setNanos(timestamp.getNano())
            .build();
    }
}
