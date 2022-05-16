package dhbw.ase.app3.search.pso;

import dhbw.ase.app2.Config;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParticleSwarmOptimization {
    private final static Logger logger = Logger.getLogger(ParticleSwarmOptimization.class);
    private final List<City> cities;
    private final PsoParameters parameters;
    private final ExecutorService executorService;

    private final List<Particle> particles;
    private final Object globalBestMutex = new Object();
    private CountDownLatch iterationLatch;
    private double globalBest = Double.MAX_VALUE;
    private Route globalBestRoute = null;

    public ParticleSwarmOptimization(List<City> cities, PsoParameters parameters) {
        this.executorService = Executors.newFixedThreadPool(Config.INSTANCE.parallelThreads);
        this.cities = Collections.unmodifiableList(cities);
        this.parameters = parameters;

        logger.info("Initialisiere ParticleSwarmOptimization (Threads: " + Config.INSTANCE.parallelThreads + ")");

        particles = new LinkedList<>();
        for (int i = 0; i < parameters.getParticleCount(); i++) {
            particles.add(new Particle(this));
        }
    }

    public Route findOptimalRoute() {
        // initialize
        iterationLatch = new CountDownLatch(particles.size());

        for (Particle particle : particles) {
            executorService.submit(particle::initialize);
        }

        try {
            iterationLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("Starte Suche");

        long iteration = 0;
        do {
            iterationLatch = new CountDownLatch(particles.size());

            for (Particle particle : particles) {
                executorService.submit(particle::runIteration);
            }

            try {
                iterationLatch.await();
            } catch (InterruptedException e) {
                logger.warn("ParticleSwarmOptimization wurde unterbrochen: %s", e.getMessage());
                throw new RuntimeException(e);
            }
        } while (++iteration < parameters.getMaxIterations() && !particlesConverged());

        // After the last iteration no tasks remain, so we do not need to wait for the service to shut down
        executorService.shutdown();

        return globalBestRoute;
    }

    public List<City> getCities() {
        return cities;
    }

    public PsoParameters getParameters() {
        return parameters;
    }

    public void countDown() {
        if (iterationLatch != null) {
            iterationLatch.countDown();
        }
    }

    public Route getGlobalBestRoute() {
        synchronized (globalBestMutex) {
            return globalBestRoute;
        }
    }

    public void checkUpdateGBest(double score, Route route) {
        synchronized (globalBestMutex) {
            if (score < globalBest) {
                logger.debug("Neue beste Route: (Länge %01.4f): %s", score, route);
                globalBest = score;
                globalBestRoute = route;
            }
        }
    }

    private boolean particlesConverged() {
        // Check if particles are converging
        double min = particles.stream().map((p) -> p.personalBest).min(Double::compareTo).get();
        double max = particles.stream().map((p) -> p.personalBest).max(Double::compareTo).get();
        logger.debug("Größter Unterschied zwischen der Fitness ist: %f", max - min);
        if (max - min < 0.001) {
            logger.info("Particle sind convergiert");
            return true;
        }
        return false;
    }
}