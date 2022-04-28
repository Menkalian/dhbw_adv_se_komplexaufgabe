package dhbw.ase.app1;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import dhbw.ase.app1.permutation.IPermutationWalker;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;

public class BruteForceOptimization {
    private final static Logger logger = Logger.getLogger(BruteForceOptimization.class);

    private final ExecutorService executor;
    private final IPermutationWalker permutationWalker;

    private final AtomicLong counter = new AtomicLong(0);
    private final Object bestMutex = new Object();
    Route bestRoute;
    double bestScore = Double.MAX_VALUE;
    private volatile boolean stop = false;

    public BruteForceOptimization(List<City> cities) {
        logger.info("Initialisiere BruteForceOptimization (Threads: " + Config.INSTANCE.parallelThreads + ")");
        executor = Executors.newFixedThreadPool(Config.INSTANCE.parallelThreads);
        permutationWalker = IPermutationWalker.getConfiguredImplementation(cities);
    }

    public Route searchOptimalRoute() {
        logger.info("Starte Suche");
        for (int i = 0 ; i < Config.INSTANCE.parallelThreads ; i++) {
            executor.submit(this::searchPermutations);
        }

        // Wait for termination of the search
        executor.shutdown();
        try {
            boolean terminated = executor.awaitTermination(Config.INSTANCE.maxDurationMinutes, TimeUnit.MINUTES);

            stop = true;
            if (!terminated) {
                // When the `stop`-Flag is set termination will occur quickly.
                terminated = executor.awaitTermination(5, TimeUnit.SECONDS);
            }
            if (!terminated) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            logger.warn("BruteForceOptimization wurde unterbrochen: %s", ex.getMessage());
        }

        return bestRoute;
    }

    private void searchPermutations() {
        double bestScore = Double.MAX_VALUE;
        Route bestRoute = null;
        long c;

        while (!stop) {
            c = counter.getAndIncrement();
            if (c >= Config.INSTANCE.maxTries) {
                break;
            }

            Route toCheck;
            try {
                toCheck = permutationWalker.nextPermutation();
            } catch (RuntimeException ex) {
                logger.error("Fehler beim Abfragen der Permutationen: %s. Breche Suche ab.", ex.getMessage());
                break;
            }

            double score = toCheck.getTotalDistance();
            logger.trace("Prüfe Route %d (Länge %.1f): %s", c, score, toCheck);

            if (score < bestScore) {
                logger.debug("Neue (lokal) beste Route: (Länge %.1f): %s", score, toCheck);
                bestScore = score;
                bestRoute = toCheck;
            }
        }

        synchronized (bestMutex) {
            if (bestScore < this.bestScore) {
                logger.debug("Neue beste Route: (Länge %.1f): %s", bestScore, bestRoute);
                this.bestScore = bestScore;
                this.bestRoute = bestRoute;
            }
        }
    }
}
