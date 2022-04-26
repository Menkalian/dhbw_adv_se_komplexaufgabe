package dhbw.ase.app1;

import dhbw.ase.app1.permutation.PermutationMode;
import dhbw.ase.log.LogLevel;
import dhbw.ase.util.loader.Dataset;

public enum Config {
    INSTANCE;

    public final int parallelThreads = Runtime.getRuntime().availableProcessors();
    public final LogLevel logLevel = LogLevel.INFO;

    public final Dataset dataset = Dataset.A280;
    public final PermutationMode permutationMode = PermutationMode.RANDOMIZED;

    public final long maxTries = 40_000_000; // Takes about a minute on my computer.
    public final long maxDurationMinutes = 2;
}
