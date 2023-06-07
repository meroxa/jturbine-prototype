package com.meroxa.turbine;

/**
 * Represents Meroxa's Turbine engine.
 */
public interface Turbine {
    // todo what if a resource is "declared" as a source,
    //  but only ever used as a destination? Should we warn about that?
    Resource resource(String name);

    /**
     * Registers a secret with the given name.
     * @param name is the name of the secret to be registered.
     */
    void registerSecret(String name);
}
