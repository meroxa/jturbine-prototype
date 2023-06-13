package com.meroxa.turbine.local;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.meroxa.turbine.*;
import com.meroxa.turbine.proto.Collection;
import com.meroxa.turbine.proto.ProcessCollectionRequest;
import com.meroxa.turbine.proto.Record;
import com.meroxa.turbine.proto.TurbineServiceGrpc;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class LocalRecords implements Records {
    private final TurbineServiceGrpc.TurbineServiceBlockingStub stub;
    private final Collection collection;
    private final String name;
    private final String stream;
    private final List<TurbineRecord> records;

    public static LocalRecords fromProtoCollection(TurbineServiceGrpc.TurbineServiceBlockingStub stub, Collection collection) {
        return new LocalRecords(
            stub,
            collection,
            collection.getName(),
            collection.getStream(),
            toTurbineRecords(collection.getRecordsList())
        );
    }

    private static List<TurbineRecord> toTurbineRecords(List<Record> protoRecords) {
        return Utils.toStream(protoRecords)
            .map(LocalRecords::toTurbineRecord)
            .collect(Collectors.toList());
    }

    private static TurbineRecord toTurbineRecord(Record record) {
        return TurbineRecord.builder()
            .key(record.getKey())
            .payload(record.getValue().toStringUtf8())
            .timestamp(toJavaTimestamp(record.getTimestamp()))
            .build();
    }

    private static LocalDateTime toJavaTimestamp(Timestamp timestamp) {
        return LocalDateTime.ofEpochSecond(
            timestamp.getSeconds(),
            timestamp.getNanos(),
            ZoneOffset.UTC
        );
    }

    @Override
    public Records process(Processor processor) {
        ProcessCollectionRequest req = ProcessCollectionRequest
            .newBuilder()
            .setProcess(
                ProcessCollectionRequest.Process
                    .newBuilder()
                    .setName("turbinehellojava")
                    .build()
            )
            .setCollection(collection)
            .build();

        Collection response = stub.addProcessToCollection(req);

        return new LocalRecords(
            stub,
            collection,
            name,
            stream,
            process(records, processor)
        );
    }

    private List<TurbineRecord> process(List<TurbineRecord> records, Processor processor) {
        return Utils.toStream(records)
            .map(r -> processor.apply(List.of(r)))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    public Collection toProtoCollection() {
        return Collection
            .newBuilder()
            .setName(getName())
            .setStream(getStream())
            .addAllRecords(getProtoRecords())
            .build();
    }

    private List<Record> getProtoRecords() {
        return Utils.toStream(getRecords())
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

    @Override
    public void writeTo(Resource resource, String collection, ConnectionOptions options) {
        resource.write(this, collection, options);
    }
}
