package com.meroxa.turbine;

import java.util.Map;

/**
 * Represents Meroxa's Turbine engine.
 */
public interface Turbine {
    RecordsCollection fromSource(String plugin, Map<String, String> config);

    Map<String, String> configFromSecret(String secretname);
}
