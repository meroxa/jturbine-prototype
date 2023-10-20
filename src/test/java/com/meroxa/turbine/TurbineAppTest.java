package com.meroxa.turbine;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TurbineAppTest {

    @Test
    void test() {
        new TurbineApp() {
            @Override
            public void setup(Turbine turbine) {
                turbine.fromSource("kafka@v0.2.0", Map.of("", ""))
                    .process(this::myProcessMethod)
                    .toDestination("s3", turbine.configFromSecret("PROD_S3"));
            }

            List<TurbineRecord> myProcessMethod(List<TurbineRecord> records) {
                return records;
            }


        };
    }

}