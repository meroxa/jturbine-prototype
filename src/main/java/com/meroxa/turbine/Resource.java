package com.meroxa.turbine;

/**
 * Represents a <a href="https://docs.meroxa.com/platform/resources/overview">resource</a> in Meroxa.
 */
public interface Resource {
    Records read(String collection, ConnectionOptions options);

    void write(Records records, String collection, ConnectionOptions options);
}
