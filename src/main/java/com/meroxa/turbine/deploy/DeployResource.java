package com.meroxa.turbine.deploy;

import com.meroxa.turbine.ConnectionOptions;
import com.meroxa.turbine.Records;
import com.meroxa.turbine.Resource;

public class DeployResource implements Resource {
    private final DeployTurbine deployTurbine;

    public DeployResource(DeployTurbine deployTurbine) {
        this.deployTurbine = deployTurbine;
    }

    @Override
    public Records read(String collection, ConnectionOptions options) {
        return new DeployRecords(deployTurbine);
    }

    @Override
    public void write(Records records, String collection, ConnectionOptions options) {

    }
}
