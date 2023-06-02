package io.meroxa.turbine;

import java.util.List;
import java.util.function.Function;

public interface Processor extends Function<List<TurbineRecord>, List<TurbineRecord>> {

}
