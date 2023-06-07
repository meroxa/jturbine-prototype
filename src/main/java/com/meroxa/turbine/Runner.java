package com.meroxa.turbine;

import com.meroxa.turbine.local.LocalTurbine;
import io.quarkus.runtime.Quarkus;

/**
 * Runs a Turbine app.
 */
public class Runner {
    public static void start(TurbineApp app) {
        String turbineCoreServer = System.getProperty("turbine.core.server");
        String appPath = System.getProperty("turbine.app.path");

        app.setup(LocalTurbine.create(turbineCoreServer, appPath));
    }
}
