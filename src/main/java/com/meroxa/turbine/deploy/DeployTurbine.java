package com.meroxa.turbine.deploy;

import com.meroxa.turbine.Processor;
import com.meroxa.turbine.Resource;
import com.meroxa.turbine.Turbine;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeployTurbine implements Turbine {
    @Override
    public Resource resource(String name) {
        return new DeployResource();
    }

    @Override
    public void registerSecret(String name) {

    }

    public Processor processor() {
        return null;
    }
}
