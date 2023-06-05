package io.meroxa.turbine.local;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import io.meroxa.turbine.*;
import io.meroxa.turbine.proto.Collection;
import io.meroxa.turbine.proto.Record;
import io.meroxa.turbine.proto.TurbineServiceGrpc;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LocalRecords implements Records {
    private final TurbineServiceGrpc.TurbineServiceBlockingStub stub;
    private final Collection collection;

    public LocalRecords(TurbineServiceGrpc.TurbineServiceBlockingStub stub,
                        Collection collection) {

        this.stub = stub;
        this.collection = collection;
    }

    @Override
    public Records process(Processor processor) {
        return new LocalRecords(
            stub,
            toTurbineRecord(collection, processor)
        );
    }

    private Collection toTurbineRecord(Collection input, Processor processor) {
        Collection.Builder processed = Collection.newBuilder();
        if (input == null) {
            return processed.build();
        }

        processed
            .setName(input.getName())
            .setStream(input.getStream())
            .addAllRecords(toTurbineRecord(input.getRecordsList(), processor));

        return processed.build();
    }

    private Iterable<Record> toTurbineRecord(List<Record> records, Processor processor) {
        return Utils.toStream(records)
            .map(this::toTurbineRecord)
            .map(r -> processor.apply(List.of(r)))
            .flatMap(List::stream)
            .map(this::toProtoRecord)
            .collect(Collectors.toList());
    }

    private Record toProtoRecord(TurbineRecord record) {
        return Record
            .newBuilder()
            .setKey(record.getKey())
            .setValue(ByteString.copyFromUtf8(record.getPayload()))
            .setTimestamp(toProtoTimestamp(record.getTimestamp()))
            .build();
    }

    private Timestamp toProtoTimestamp(LocalDateTime timestamp) {
        return Timestamp.newBuilder()
            .setSeconds(timestamp.getSecond())
            .setNanos(timestamp.getNano())
            .build();
    }

    private TurbineRecord toTurbineRecord(Record record) {
        return TurbineRecord.builder()
            .key(record.getKey())
            .payload(record.getValue().toString())
            .timestamp(toJavaTimestamp(record.getTimestamp()))
            .build();
    }

    private LocalDateTime toJavaTimestamp(Timestamp timestamp) {
        return LocalDateTime.ofEpochSecond(
            timestamp.getSeconds(),
            timestamp.getNanos(),
            ZoneOffset.UTC
        );
    }

    @Override
    public void writeTo(Resource resource, String collection, ConnectionOptions options) {
        resource.write(this, collection, options);
    }
}
