package com.meroxa.turbine;

import com.meroxa.turbine.deploy.DeployTurbine;
import com.meroxa.turbine.run.LocalTurbine;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class Main implements QuarkusApplication {
    private static final Logger logger = Logger.getLogger(Main.class);
    public static final String MODE_DEPLOY = "deploy";
    public static final String MODE_LOCAL = "local";

    @Inject
    Instance<TurbineApp> turbineApp;
    @Inject
    DeployTurbine deployTurbine;

    @Override
    public int run(String... args) {
        logger.infof("Invoking run() in %s", Main.class);

        String mode = System.getProperty("turbine.mode", MODE_DEPLOY);
        logger.infof("turbine.mode is %s", mode);

        mode = mode.toLowerCase();
        if (!mode.equals(MODE_LOCAL) && !mode.equals(MODE_DEPLOY)) {
            throw new IllegalArgumentException("unknown mode: " + mode);
        }

        logger.infof("turbineApp: %s", turbineApp);
        logger.infof("turbineApp: %s", turbineApp.get());
        if (mode.equals(MODE_LOCAL)) {
            String turbineCoreServer = System.getProperty("turbine.core.server");
            String appPath = System.getProperty("turbine.app.path");

            turbineApp.get().setup(LocalTurbine.create(turbineCoreServer, appPath));
        } else {
            turbineApp.get().setup(deployTurbine);
            Quarkus.waitForExit();
        }

        return 0;
    }
}
