package com.meroxa.turbine.deploy;

import com.meroxa.turbine.ConnectionOptions;
import com.meroxa.turbine.Records;
import com.meroxa.turbine.Resource;

public class DeployResource implements Resource {
    @Override
    public Records read(String collection, ConnectionOptions options) {
        return new DeployRecords();
    }

    @Override
    public void write(Records records, String collection, ConnectionOptions options) {

    }
}
