package com.meroxa.turbine.deploy;

import java.util.Map;

import com.meroxa.turbine.Processor;
import com.meroxa.turbine.RecordsCollection;

public class DeployRecords implements RecordsCollection {
    private final DeployTurbine deployTurbine;

    public DeployRecords(DeployTurbine deployTurbine) {
        this.deployTurbine = deployTurbine;
    }

    @Override
    public RecordsCollection process(Processor processor) {
        deployTurbine.setProcessor(processor);
        return this;
    }

    @Override
    public void toDestination(String plugin, Map<String, String> config) {

    }
}
