package com.meroxa.turbine;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class Main implements QuarkusApplication {
    @Inject
    Instance<TurbineApp> turbineApp;

    @Override
    public int run(String... args) {
        System.out.printf("Invoking run() in %s%n", Main.class);

        String mode = System.getProperty("turbine.mode");
        System.out.printf("turbine.mode is %s%n", mode);
        if (mode == null) {
            throw new IllegalStateException("turbine.mode not specified");
        }
        mode = mode.toLowerCase();
        if (!mode.equals("local") && !mode.equals("deploy")) {
            throw new IllegalArgumentException("unknown mode: " + mode);
        }

        if (mode.equals("local")) {
            System.out.printf("turbineApp: %s%n", turbineApp);
            System.out.printf("turbineApp: %s%n", turbineApp.get());
            Runner.start(turbineApp.get());
        } else {
            Quarkus.waitForExit();
        }

        return 0;
    }
}
