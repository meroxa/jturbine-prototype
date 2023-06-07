package com.meroxa.turbine.deploy;

import com.meroxa.turbine.ConnectionOptions;
import com.meroxa.turbine.Processor;
import com.meroxa.turbine.Records;
import com.meroxa.turbine.Resource;

public class DeployRecords implements Records {
    @Override
    public Records process(Processor processor) {
        return null;
    }

    @Override
    public void writeTo(Resource resource, String collection, ConnectionOptions options) {

    }
}
