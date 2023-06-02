package io.meroxa.turbine.local;

import io.meroxa.turbine.ConnectionOptions;
import io.meroxa.turbine.Processor;
import io.meroxa.turbine.Records;
import io.meroxa.turbine.Resource;
import io.meroxa.turbine.proto.Collection;
import io.meroxa.turbine.proto.TurbineServiceGrpc;
import lombok.Getter;

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
        return this;
    }

    @Override
    public void writeTo(Resource resource, String collection, ConnectionOptions options) {
        resource.write(this, collection, options);
    }
}
