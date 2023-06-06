package com.meroxa.turbine;

import java.util.Collection;

public class TestTurbine implements Turbine {
    @Override
    public Resource resource(String name) {
        return null;
    }

    @Override
    public void registerSecret(String name) {

    }

    /**
     * Registers a source, for which the data will be loaded from the given file.
     */
    public TestTurbine withSource(String name, String path) {
        return null;
    }

    /**
     * Registers a source, for which the data will be returned from the given collection.
     */
    public TestTurbine withSource(String name, Collection<TurbineRecord> records) {
        return null;
    }

    /**
     * Registers a destination.
     *
     * @param name
     * @param resource
     * @return
     */
    public TestTurbine withDestination(String name, Resource resource) {
        return null;
    }
}
