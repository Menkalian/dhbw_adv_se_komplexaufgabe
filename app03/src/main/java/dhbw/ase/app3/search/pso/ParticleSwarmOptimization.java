package dhbw.ase.app3.search.pso;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app3.Config;
import dhbw.ase.app3.OptimizationParameter;
import dhbw.ase.app3.ParameterRange;
import dhbw.ase.log.Logger;
import dhbw.ase.random.MersenneTwisterFast;
import dhbw.ase.tsp.City;

/**
 * Non parallelized particle swarm optimization for ABC-Parameters.
 * This is not multithreaded, since the calculation of the fitness is already multithreaded.
 * A parallelization of this would only increase the load on the scheduler.
 */
public class ParticleSwarmOptimization {
    private final static Logger logger = Logger.getLogger(ParticleSwarmOptimization.class);
    private final ParticleSwarmOptimizationParameters params;
    private final List<City> cities;

    private final List<Particle> particles;
    private double globalBest = Double.MAX_VALUE;
    private Map<OptimizationParameter, Double> globalBestParameters = null;

    public ParticleSwarmOptimization(ParticleSwarmOptimizationParameters params, List<City> cities) {
        this.params = params;
        this.cities = cities;

        particles = new LinkedList<>();
        for (int i = 0 ; i < params.getParticleCount() ; i++) {
            particles.add(new Particle(this));
        }
    }

    public ArtificialBeeColonyParameters findOptimalParameters() {
        // initialize
        for (Particle particle : particles) {
            particle.initialize();
        }

        logger.info("Starte Suche");
        long iteration = 0;
        do {
            for (Particle particle : particles) {
                particle.runIteration();
            }
        } while (++iteration < params.getMaxIterations() && !particlesConverged());

        // Build result
        ArtificialBeeColonyParameters parameters = new ArtificialBeeColonyParameters(
                0L, 0, 0, 0, new HashMap<>());
        for (OptimizationParameter param : this.globalBestParameters.keySet()) {
            parameters = param.getParameterApplyFunction().applyValue(parameters, this.globalBestParameters.get(param));
        }
        return parameters;
    }

    public Map<OptimizationParameter, Double> getRandomPosition() {
        MersenneTwisterFast rng = new MersenneTwisterFast(System.nanoTime());

        Map<OptimizationParameter, Double> position = new HashMap<>();
        for (OptimizationParameter key : Config.INSTANCE.parameterRanges.keySet()) {
            ParameterRange<Double> range = Config.INSTANCE.parameterRanges.get(key);
            position.put(key, range.getMin() + rng.nextDouble() * (range.getMax() - range.getMin()));
        }
        return position;
    }

    public List<City> getCities() {
        return cities;
    }

    public ParticleSwarmOptimizationParameters getPsoParameters() {
        return params;
    }

    public Map<OptimizationParameter, Double> getGlobalBestParameters() {
        return globalBestParameters;
    }

    public void checkUpdateGBest(double score, Map<OptimizationParameter, Double> parameters) {
        if (score < globalBest) {
            logger.debug("Neue beste Parameter: (Score %.1f): %s", score, parameters);
            globalBest = score;
            globalBestParameters = parameters;
        }
    }

    private boolean particlesConverged() {
        // Check if particles are converging
        double min = particles.stream().map(Particle::getPersonalBest).min(Double::compareTo).get();
        double max = particles.stream().map(Particle::getPersonalBest).max(Double::compareTo).get();
        logger.debug("Größter Unterschied zwischen der Fitness ist: %f", max - min);
        if (max - min < 0.001) {
            logger.info("Particle sind konvergiert");
            return true;
        }
        return false;
    }
}