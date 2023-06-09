package com.meroxa.turbine.deploy;

import com.meroxa.turbine.Processor;
import com.meroxa.turbine.Resource;
import com.meroxa.turbine.Turbine;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeployTurbine implements Turbine {
    private Processor processor;

    @Override
    public Resource resource(String name) {
        return new DeployResource(this);
    }

    @Override
    public void registerSecret(String name) {

    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }
}
