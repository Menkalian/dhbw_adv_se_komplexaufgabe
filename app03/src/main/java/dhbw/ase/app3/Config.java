package dhbw.ase.app3;

import java.util.Map;

import dhbw.ase.app3.search.SearchMethod;
import dhbw.ase.log.LogLevel;
import dhbw.ase.util.loader.Dataset;

import static dhbw.ase.app3.OptimizationParameter.*;

public enum Config {
    INSTANCE;

    public final Dataset dataset = Dataset.A280;
    public final LogLevel logLevel = LogLevel.TRACE;
    public final LogLevel consoleLogLevel = LogLevel.TRACE;
    public final LogLevel fileLogLevel = LogLevel.TRACE;
    public final String logFilePath = null;

    public final int sampleSizePerConfiguration = 2;
    public final SearchMethod searchMethod = SearchMethod.BRUTE_FORCE;
    public final LogLevel executionLogLevel = LogLevel.SYSTEM;

    public final Map<OptimizationParameter, ParameterRange<Double>> parameterRanges = Map.of(
            ITERATION, new ParameterRange<>(100_000.0, 100_000.0, i -> i + 50_000),
            FOOD_SOURCES, new ParameterRange<>(30.0, 30.0, i -> i + 1),
            ONLOOKER_BEES, new ParameterRange<>(30.0, 30.0, i -> i + 1),
            REVISIT_LIMIT, new ParameterRange<>(5000.0, 15_000.0, i -> i + 5000),
            POINT_SWAP_WEIGHT, new ParameterRange<>(0.0, 3.0, i -> i + 1),
            BLOCK_SWAP_WEIGHT, new ParameterRange<>(0.0, 3.0, i -> i + 1),
            SINGLE_SHIFT_WEIGHT, new ParameterRange<>(0.0, 3.0, i -> i + 1),
            WEIGHTED_SINGLE_SHIFT_WEIGHT, new ParameterRange<>(0.0, 3.0, i -> i + 1),
            PARTIAL_REVERSE_WEIGHT, new ParameterRange<>(0.0, 3.0, i -> i + 1),
            PARTIAL_SHIFT_WEIGHT, new ParameterRange<>(0.0, 3.0, i -> i + 1)
    );
}
