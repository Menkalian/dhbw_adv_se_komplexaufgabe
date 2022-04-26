package dhbw.ase.app2.abc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import dhbw.ase.app2.Config;
import dhbw.ase.log.Logger;
import dhbw.ase.random.MersenneTwisterFast;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;

public class ArtificialBeeColonyOptimization {
    private static final Logger logger = Logger.getLogger(ArtificialBeeColonyOptimization.class);

    private final ExecutorService executorService;
    private final MersenneTwisterFast rng;

    private final ArtificialBeeColonyParameters parameters;
    private final List<EmployedBee> employedBees;
    private final List<City> cities;

    private final Object globalBestMutex = new Object();
    private double globalBestScore = Double.MAX_VALUE;
    private Route globalBestRoute = null;

    private CountDownLatch iterationLatch;
    private List<Double> probabilities;

    public ArtificialBeeColonyOptimization(List<City> cities, ArtificialBeeColonyParameters parameters) {
        logger.info("Initialisiere ArtificialBeeColonyOptimization (Threads: " + Config.INSTANCE.parallelThreads + ")");

        this.executorService = Executors.newFixedThreadPool(Config.INSTANCE.parallelThreads);
        this.rng = new MersenneTwisterFast(System.nanoTime());

        this.cities = Collections.unmodifiableList(cities);
        this.parameters = parameters;

        logger.debug("Erstelle %d EmployedBees/FoodSources", parameters.getFoodSourceCount());
        employedBees = new ArrayList<>(parameters.getFoodSourceCount());
        for (int i = 0 ; i < parameters.getFoodSourceCount() ; i++) {
            employedBees.add(new EmployedBee(this));
        }
    }

    public Route findOptimalRoute() {
        logger.system("Suche optimale Route... (Parameter: %s)", parameters);

        for (long iteration = 0 ; iteration < parameters.getMaxIterations() ; iteration++) {
            logger.debug("Starte Iteration %d", iteration);

            // Scout
            iterationLatch = new CountDownLatch(employedBees.size());
            for (EmployedBee bee : employedBees) {
                executorService.submit(bee::scoutIfNecessary);
            }
            waitForLatch();

            // Employed exploration
            iterationLatch = new CountDownLatch(employedBees.size());
            for (EmployedBee bee : employedBees) {
                executorService.submit(() -> bee.explore(false));
            }
            waitForLatch();

            // Onlooker exploration
            updateProbabilities();
            iterationLatch = new CountDownLatch(parameters.getOnlookerBeeCount());

            // Perform roulette selection
            int foodSourceIndex = 0;
            for (int i = 0 ; i < parameters.getOnlookerBeeCount() ; i++) {
                synchronized (rng) {
                    while (rng.nextDouble() >= probabilities.get(foodSourceIndex)) {
                        foodSourceIndex = (foodSourceIndex + 1) % probabilities.size();
                    }
                }

                // Final variable for usage in task
                final int foodIndexToUse = foodSourceIndex;
                executorService.submit(() -> {
                    EmployedBee explorationPoint = employedBees.get(foodIndexToUse);
                    explorationPoint.explore(true);
                });

                // Increment to look at another source next time
                foodSourceIndex = (foodSourceIndex + 1) % probabilities.size();
            }
            waitForLatch();
        }

        // After the last iteration no tasks remain, so we do not need to wait for the service to finish its tasks.
        executorService.shutdown();

        return globalBestRoute;
    }

    public void checkUpdateGlobalBest(double score, Route route) {
        synchronized (globalBestMutex) {
            if (score < globalBestScore) {
                logger.info("Neue beste Route: (Länge %.4f): %s", score, route);
                globalBestScore = score;
                globalBestRoute = route;
            }
        }
    }

    void countDown() {
        if (iterationLatch != null) {
            iterationLatch.countDown();
        }
    }

    ArtificialBeeColonyParameters getParameters() {
        return parameters;
    }

    Route getBaseRoute() {
        return new Route(cities);
    }

    private void waitForLatch() {
        try {
            iterationLatch.await();
        } catch (InterruptedException e) {
            logger.error("Wartevorgang wurde unterbrochen: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Algorithm used from the given sources
    private void updateProbabilities() {
        double bestScore = employedBees
                .stream()
                .mapToDouble(EmployedBee::getCurrentScore)
                .min()
                .orElse(Double.MAX_VALUE);
        probabilities = employedBees
                .stream()
                .map(bee -> (0.9 * (bestScore / bee.getCurrentScore()) + 0.1))
                .collect(Collectors.toList());
    }
}
