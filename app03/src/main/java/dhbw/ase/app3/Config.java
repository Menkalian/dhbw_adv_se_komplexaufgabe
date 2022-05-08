package dhbw.ase.app3;

import dhbw.ase.app3.search.SearchMethod;
import dhbw.ase.log.LogLevel;
import dhbw.ase.util.loader.Dataset;

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

    public final ParameterRange<Integer> iterationRange =
            new ParameterRange<>(100_000, 100_000, i -> i + 50_000);
    public final ParameterRange<Integer> foodSourceRange =
            new ParameterRange<>(30, 30, i -> i + 1);
    public final ParameterRange<Integer> onlookerBeeRange =
            new ParameterRange<>(30, 30, i -> i + 1);
    public final ParameterRange<Integer> revisitLimitRange =
            new ParameterRange<>(5000, 15_000, i -> i + 5000);
    public final ParameterRange<Integer> pointSwapWeight =
            new ParameterRange<>(0, 3, i -> i + 1);
    public final ParameterRange<Integer> blockSwapWeight =
            new ParameterRange<>(0, 3, i -> i + 1);
    public final ParameterRange<Integer> singleShiftWeight =
            new ParameterRange<>(0, 3, i -> i + 1);
    public final ParameterRange<Integer> partialReverseWeight =
            new ParameterRange<>(0, 3, i -> i + 1);
    public final ParameterRange<Integer> partialShiftWeight =
            new ParameterRange<>(0, 3, i -> i + 1);
}
