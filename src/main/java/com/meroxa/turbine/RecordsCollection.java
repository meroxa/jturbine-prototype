package com.meroxa.turbine;

import java.util.Map;

/**
 * Represents a (collection) stream of {@link TurbineRecord}s.
 */
public interface RecordsCollection {
    /**
     * Registers a processor on this collection (stream) of records.
     * @return The same collection (stream) of records.
     */
    // TODO Make Records immutable and return a new instance every time process() is called.
    RecordsCollection process(Processor processor);

    void toDestination(String plugin, Map<String, String> config);
}
