package com.meroxa.turbine.run;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.meroxa.turbine.Processor;
import com.meroxa.turbine.RecordsCollection;
import com.meroxa.turbine.TurbineRecord;
import com.meroxa.turbine.Utils;
import com.meroxa.turbine.proto.*;
import com.meroxa.turbine.proto.Record;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LocalRecordsCollection implements RecordsCollection {
    private final TurbineServiceGrpc.TurbineServiceBlockingStub coreClient;
    private final List<TurbineRecord> records;

    public static LocalRecordsCollection fromProtoCollection(TurbineServiceGrpc.TurbineServiceBlockingStub stub,
                                                             com.meroxa.turbine.proto.RecordsCollection collection) {
        return new LocalRecordsCollection(
            stub,
            toTurbineRecords(collection.getRecordsList())
        );
    }

    public static List<TurbineRecord> toTurbineRecords(List<Record> protoRecords) {
        return Utils.toStream(protoRecords)
            .map(LocalRecordsCollection::toTurbineRecord)
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
    public RecordsCollection process(Processor processor) {
        ProcessRecordsRequest req = ProcessRecordsRequest
            .newBuilder()
            .setProcess(
                ProcessRecordsRequest.Process
                    .newBuilder()
                    .setName("turbinehellojava")
                    .build()
            )
            .setRecords(com.meroxa.turbine.proto.RecordsCollection.newBuilder().addAllRecords(getProtoRecords()).build())
            .build();

        com.meroxa.turbine.proto.RecordsCollection response = coreClient.process(req);

        return new LocalRecordsCollection(
            coreClient,
            process(records, processor)
        );
    }

    private List<TurbineRecord> process(List<TurbineRecord> records, Processor processor) {
        return Utils.toStream(records)
            .map(r -> processor.apply(List.of(r)))
            .flatMap(List::stream)
            .toList();
    }

    public com.meroxa.turbine.proto.RecordsCollection toProtoCollection() {
        return com.meroxa.turbine.proto.RecordsCollection
            .newBuilder()
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
    public void toDestination(String plugin, Map<String, String> configs) {
        var configurations = Configurations.newBuilder();

        for (Map.Entry<String, String> kv : configs.entrySet()) {
            var c = Configuration.newBuilder().setField(kv.getKey()).setValue(kv.getValue()).build();
            configurations.addConfiguration(c);
        }

        var records = com.meroxa.turbine.proto.RecordsCollection
            .newBuilder()
            .addAllRecords(getProtoRecords())
            .build();

        WriteToDestinationRequest request = WriteToDestinationRequest
            .newBuilder()
            .setPluginName(plugin)
            .setConfiguration(configurations)
            .setRecords(records)
            .build();

        coreClient.writeToDestination(request);
    }
}
