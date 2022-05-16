package dhbw.ase.app2;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app2.abc.NeighbourFindingMethod;
import dhbw.ase.log.LogLevel;
import dhbw.ase.util.loader.Dataset;

public enum Config {
    INSTANCE;

    public final int parallelThreads = Runtime.getRuntime().availableProcessors();
    public final LogLevel logLevel = LogLevel.INFO;
    public final LogLevel consoleLogLevel = LogLevel.INFO;
    public final LogLevel fileLogLevel = LogLevel.WARN;
    public final String logFilePath = MessageFormat.format(
            "logs/log_{0}.txt",
            LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")));

    public final Dataset dataset = Dataset.A280;
    public final ArtificialBeeColonyParameters defaultAlgorithmParameters = new ArtificialBeeColonyParameters(
            200_000,
            50,
            50,
            15_000,
            Map.of(
                    NeighbourFindingMethod.POINT_SWAP, 8,
                    NeighbourFindingMethod.BLOCK_SWAP, 1,
                    NeighbourFindingMethod.SINGLE_SHIFT, 20,
                    NeighbourFindingMethod.PARTIAL_REVERSE, 40,
                    NeighbourFindingMethod.WEIGHTED_SINGLE_SHIFT, 30,
                    NeighbourFindingMethod.PARTIAL_SHIFT, 1));
}
