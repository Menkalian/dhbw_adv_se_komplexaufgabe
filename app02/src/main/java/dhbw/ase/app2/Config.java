package dhbw.ase.app2;

import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.log.LogLevel;
import dhbw.ase.util.loader.Dataset;

public enum Config {
    INSTANCE;

    public final int parallelThreads = Runtime.getRuntime().availableProcessors();
    public final LogLevel logLevel = LogLevel.INFO;

    public final Dataset dataset = Dataset.A280;
    public final ArtificialBeeColonyParameters defaultAlgorithmParameters = new ArtificialBeeColonyParameters(
            300_000,
            30,
            30,
            15000,
            1
    );
}
