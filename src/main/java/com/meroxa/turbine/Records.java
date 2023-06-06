package com.meroxa.turbine;

/**
 * Represents a (collection) stream of {@link TurbineRecord}s.
 */
public interface Records {
    /**
     * Registers a processor on this collection (stream) of records.
     * @return The same collection (stream) of records.
     */
    // TODO Make Records immutable and return a new instance every time process() is called.
    Records process(Processor processor);

    /**
     * Writes this collection (stream) of records into the given resource and collection,
     * and using the given ConnectionOptions.
     *
     * Semantically the same as {@link Resource#write(Records, String, ConnectionOptions)},
     * but exists to make chaining of method calls easier.
     */
    void writeTo(Resource resource, String collection, ConnectionOptions options);
}
