package io.meroxa.turbine;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;

public class Main implements QuarkusApplication {
    @Override
    public int run(String... args) {
        System.out.println("inside io.meroxa.turbine.Main");
        Quarkus.waitForExit();

        return 0;
    }
}
