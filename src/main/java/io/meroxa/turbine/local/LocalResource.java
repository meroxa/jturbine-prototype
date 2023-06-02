package io.meroxa.turbine.local;

import com.google.protobuf.Empty;
import io.meroxa.turbine.ConnectionOptions;
import io.meroxa.turbine.Records;
import io.meroxa.turbine.Resource;
import io.meroxa.turbine.proto.Collection;
import io.meroxa.turbine.proto.ReadCollectionRequest;
import io.meroxa.turbine.proto.TurbineServiceGrpc;
import io.meroxa.turbine.proto.WriteCollectionRequest;
import org.jboss.logging.Logger;

public class LocalResource implements Resource {
    private static final Logger logger = Logger.getLogger(LocalResource.class);
    private final TurbineServiceGrpc.TurbineServiceBlockingStub stub;
    private final io.meroxa.turbine.proto.Resource resource;

    public LocalResource(TurbineServiceGrpc.TurbineServiceBlockingStub stub,
                         io.meroxa.turbine.proto.Resource resource) {

        this.stub = stub;
        this.resource = resource;
    }

    @Override
    public Records read(String collectionName, ConnectionOptions options) {
        logger.infof("reading records %s with options %s", collectionName, options);

        ReadCollectionRequest request = ReadCollectionRequest
            .newBuilder()
            .setResource(resource)
            .setCollection(collectionName)
            .build();

        Collection collection = stub.readCollection(request);
        return new LocalRecords(stub, collection);
    }

    @Override
    public void write(Records records, String collection, ConnectionOptions options) {
        logger.infof("writing records %s with options %s", collection, options);
        WriteCollectionRequest req = WriteCollectionRequest
            .newBuilder()
            .setResource(resource)
            .setSourceCollection(((LocalRecords) records).getCollection())
            .setTargetCollection(collection)
            .build();

        Empty empty = stub.writeCollectionToResource(req);
    }
}
