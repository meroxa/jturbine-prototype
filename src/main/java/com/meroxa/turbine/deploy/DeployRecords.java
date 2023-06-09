package com.meroxa.turbine.deploy;

import com.meroxa.turbine.ConnectionOptions;
import com.meroxa.turbine.Processor;
import com.meroxa.turbine.Records;
import com.meroxa.turbine.Resource;

public class DeployRecords implements Records {
    private final DeployTurbine deployTurbine;

    public DeployRecords(DeployTurbine deployTurbine) {
        this.deployTurbine = deployTurbine;
    }

    @Override
    public Records process(Processor processor) {
        deployTurbine.setProcessor(processor);
        return this;
    }

    @Override
    public void writeTo(Resource resource, String collection, ConnectionOptions options) {

    }
}
