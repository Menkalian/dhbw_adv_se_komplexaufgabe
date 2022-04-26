package dhbw.ase.app3;

import dhbw.ase.app3.search.SearchMethod;
import dhbw.ase.log.LogLevel;
import dhbw.ase.util.loader.Dataset;

public enum Config {
    INSTANCE;

    public final LogLevel logLevel = LogLevel.INFO;
    public final Dataset dataset = Dataset.A280;

    public final int sampleSizePerConfiguration = 2;
    public final SearchMethod searchMethod = SearchMethod.BRUTE_FORCE;

    public final ParameterRange<Integer> iterationRange =
            new ParameterRange<>(100_000, 100_000, i -> i + 50_000);
    public final ParameterRange<Integer> foodSourceRange =
            new ParameterRange<>(28, 32, i -> i + 1);
    public final ParameterRange<Integer> onlookerBeeRange =
            new ParameterRange<>(28, 32, i -> i + 1);
    public final ParameterRange<Integer> revisitLimitRange =
            new ParameterRange<>(5000, 15_000, i -> i + 500);
    public final ParameterRange<Integer> revisitExplorationRadius =
            new ParameterRange<>(0, 2, i -> i + 1);
}
