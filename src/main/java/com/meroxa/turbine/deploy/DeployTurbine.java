package com.meroxa.turbine.deploy;

import java.util.Map;

import com.meroxa.turbine.Processor;
import com.meroxa.turbine.RecordsCollection;
import com.meroxa.turbine.Turbine;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeployTurbine implements Turbine {
    private Processor processor;

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public RecordsCollection fromSource(String plugin, Map<String, String> config) {
        return new DeployRecords(this);
    }

    @Override
    public Map<String, String> configFromSecret(String secretname) {
        return null;
    }
}
