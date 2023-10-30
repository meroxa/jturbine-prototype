package com.meroxa.turbine.run;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.meroxa.turbine.Processor;
import com.meroxa.turbine.RecordsCollection;
import com.meroxa.turbine.TurbineRecord;
import com.meroxa.turbine.Utils;
import com.meroxa.turbine.proto.Configuration;
import com.meroxa.turbine.proto.Configurations;
import com.meroxa.turbine.proto.ProcessRecordsRequest;
import com.meroxa.turbine.proto.Record;
import com.meroxa.turbine.proto.TurbineServiceGrpc;
import com.meroxa.turbine.proto.WriteToDestinationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jboss.logging.Logger;

@AllArgsConstructor
@Getter
public class LocalRecordsCollection implements RecordsCollection {
    private static final Logger logger = Logger.getLogger(LocalRecordsCollection.class);

    private final TurbineServiceGrpc.TurbineServiceBlockingStub coreClient;
    private final String sourceStreamName;
    private final List<TurbineRecord> records;

    public static LocalRecordsCollection fromProtoCollection(TurbineServiceGrpc.TurbineServiceBlockingStub stub,
                                                             com.meroxa.turbine.proto.RecordsCollection collection) {
        return new LocalRecordsCollection(
            stub,
            collection.getStream(),
            toTurbineRecords(collection.getRecordsList())
        );
    }

    public static List<TurbineRecord> toTurbineRecords(List<Record> protoRecords) {
        return Utils.toStream(protoRecords)
            .map(LocalRecordsCollection::toTurbineRecord)
            .toList();
    }

    @SneakyThrows
    static TurbineRecord toTurbineRecord(Record record) {
        logger.infof("transform proto record: %s", String.valueOf(record));
        String payload = new ObjectMapper()
            .readTree(record.getValue().toStringUtf8())
            .get("payload")
            .toString();

        return TurbineRecord.builder()
            .key(record.getKey())
            .payload(payload)
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
        com.meroxa.turbine.proto.RecordsCollection recordsCollection = com.meroxa.turbine.proto.RecordsCollection
            .newBuilder()
            .setStream(getSourceStreamName())
            .addAllRecords(getProtoRecords())
            .build();
        ProcessRecordsRequest req = ProcessRecordsRequest
            .newBuilder()
            .setProcess(
                ProcessRecordsRequest.Process
                    .newBuilder()
                    .setName("turbinehellojava")
                    .build()
            )
            .setRecords(recordsCollection)
            .build();

        com.meroxa.turbine.proto.RecordsCollection response = coreClient.process(req);

        return new LocalRecordsCollection(
            coreClient,
            getSourceStreamName(),
            process(records, processor)
        );
    }

    private List<TurbineRecord> process(List<TurbineRecord> records, Processor processor) {
        logger.infof("processing records: %s", String.valueOf(records));

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
            .setStream(getSourceStreamName())
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
