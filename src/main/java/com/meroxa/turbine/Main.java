package com.meroxa.turbine;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;

public class Main implements QuarkusApplication {
    @Override
    public int run(String... args) {
        System.out.println("inside com.meroxa.turbine.Main");
        Quarkus.waitForExit();

        return 0;
    }
}
