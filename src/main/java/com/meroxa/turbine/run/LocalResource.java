package com.meroxa.turbine.run;

import com.google.protobuf.Empty;
import com.meroxa.turbine.proto.Collection;
import com.meroxa.turbine.proto.Resource;
import com.meroxa.turbine.proto.WriteCollectionRequest;
import com.meroxa.turbine.ConnectionOptions;
import com.meroxa.turbine.Records;
import com.meroxa.turbine.proto.ReadCollectionRequest;
import com.meroxa.turbine.proto.TurbineServiceGrpc;
import org.jboss.logging.Logger;

public class LocalResource {
    private static final Logger logger = Logger.getLogger(LocalResource.class);
    private final TurbineServiceGrpc.TurbineServiceBlockingStub stub;
    private final Resource resource;

    public LocalResource(TurbineServiceGrpc.TurbineServiceBlockingStub stub,
                         Resource resource) {

        this.stub = stub;
        this.resource = resource;
    }

    public Records read(String collectionName, ConnectionOptions options) {
        logger.infof("reading records %s with options %s", collectionName, options);

        ReadCollectionRequest request = ReadCollectionRequest
            .newBuilder()
            .setResource(resource)
            .setCollection(collectionName)
            .build();

        Collection collection = stub.readCollection(request);
        return LocalRecords.fromProtoCollection(stub, collection);
    }

    public void write(Records records, String collection, ConnectionOptions options) {
        logger.infof("writing records %s with options %s", collection, options);
        WriteCollectionRequest req = WriteCollectionRequest
            .newBuilder()
            .setResource(resource)
            .setSourceCollection(((LocalRecords) records).toProtoCollection())
            .setTargetCollection(collection)
            .build();

        Empty empty = stub.writeCollectionToResource(req);
    }
}
