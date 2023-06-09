package com.meroxa.turbine;

import com.meroxa.turbine.deploy.DeployTurbine;
import com.meroxa.turbine.local.LocalResource;
import com.meroxa.turbine.local.LocalTurbine;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class Main implements QuarkusApplication {
    private static final Logger logger = Logger.getLogger(Main.class);

    @Inject
    Instance<TurbineApp> turbineApp;
    @Inject
    FunctionService functionService;

    @Override
    public int run(String... args) {
        logger.infof("Invoking run() in %s", Main.class);

        String mode = System.getProperty("turbine.mode");
        logger.infof("turbine.mode is %s", mode);
        if (mode == null) {
            throw new IllegalStateException("turbine.mode not specified");
        }
        mode = mode.toLowerCase();
        if (!mode.equals("local") && !mode.equals("deploy")) {
            throw new IllegalArgumentException("unknown mode: " + mode);
        }

        logger.infof("turbineApp: %s", turbineApp);
        logger.infof("turbineApp: %s", turbineApp.get());
        if (mode.equals("local")) {
            String turbineCoreServer = System.getProperty("turbine.core.server");
            String appPath = System.getProperty("turbine.app.path");

            turbineApp.get().setup(LocalTurbine.create(turbineCoreServer, appPath));
        } else {
            turbineApp.get().setup(functionService);
            Quarkus.waitForExit();
        }

        return 0;
    }
}
