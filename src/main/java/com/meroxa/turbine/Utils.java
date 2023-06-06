package com.meroxa.turbine;

import java.util.Collection;
import java.util.stream.Stream;

public final class Utils {
    private Utils() {

    }

    public static <T> Stream<T> toStream(Collection<T> collection) {
        if (collection == null) {
            return Stream.empty();
        }

        return collection.stream();
    }
}
