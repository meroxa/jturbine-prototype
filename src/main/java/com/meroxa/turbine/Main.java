package com.meroxa.turbine;

import com.meroxa.turbine.local.LocalResource;
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

        if (mode.equals("local")) {
            logger.infof("turbineApp: %s", turbineApp);
            logger.infof("turbineApp: %s", turbineApp.get());
            Runner.start(turbineApp.get());
        } else {
            Quarkus.waitForExit();
        }

        return 0;
    }
}
