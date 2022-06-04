package dhbw.ase.app3.search.pso;

import java.util.HashMap;
import java.util.Map;

import dhbw.ase.app2.abc.ArtificialBeeColonyOptimization;
import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app3.Config;
import dhbw.ase.app3.OptimizationParameter;
import dhbw.ase.app3.ParameterRange;
import dhbw.ase.log.Logger;
import dhbw.ase.random.MersenneTwisterFast;
import dhbw.ase.tsp.Route;

public class Particle {
    private static long instanceCount = 0;
    private final long id = instanceCount++;
    private final Logger logger = Logger.getLogger("Partikel " + id);
    private final ParticleSwarmOptimization sharedState;
    private final MersenneTwisterFast rng;

    private double personalBest = Double.MAX_VALUE;
    private Map<OptimizationParameter, Double> personalBestParameters;

    private Map<OptimizationParameter, Double> parameters;
    private Map<OptimizationParameter, Double> velocity;

    public Particle(ParticleSwarmOptimization pso) {
        sharedState = pso;
        rng = new MersenneTwisterFast(System.nanoTime());
    }

    public void initialize() {
        parameters = sharedState.getRandomPosition();
        velocity = difference(parameters, sharedState.getRandomPosition());

        updatePersonalBest();
    }

    public void runIteration() {
        updateVelocity();
        updateCoordinates();
        updatePersonalBest();
    }

    private void updateVelocity() {
        ParticleSwarmOptimizationParameters p = sharedState.getPsoParameters();

        double cognitiveFactor = p.getCognitiveRatio() * rng.nextDouble();
        Map<OptimizationParameter, Double> cognitiveVelocity = difference(personalBestParameters, parameters);

        double socialFactor = p.getSocialRatio() * rng.nextDouble();
        Map<OptimizationParameter, Double> socialVelocity = difference(sharedState.getGlobalBestParameters(), parameters);

        multiply(velocity, p.getInertia());
        multiply(cognitiveVelocity, cognitiveFactor);
        multiply(socialVelocity, socialFactor);
        velocity = add(velocity, add(cognitiveVelocity, socialVelocity));
        logger.trace("Neue Partikelgeschwindigkeit: %s", velocity.toString());
    }

    private Map<OptimizationParameter, Double> difference(Map<OptimizationParameter, Double> minuend, Map<OptimizationParameter, Double> subtrahend) {
        Map<OptimizationParameter, Double> result = new HashMap<>();
        for (OptimizationParameter key : minuend.keySet()) {
            result.put(key, minuend.get(key) - subtrahend.get(key));
        }
        return result;
    }

    private Map<OptimizationParameter, Double> add(Map<OptimizationParameter, Double> summand1, Map<OptimizationParameter, Double> summand2) {
        Map<OptimizationParameter, Double> result = new HashMap<>();
        for (OptimizationParameter key : summand1.keySet()) {
            result.put(key, summand1.get(key) + summand2.get(key));
        }
        return result;
    }

    private void multiply(Map<OptimizationParameter, Double> velocity, double factor) {
        velocity.replaceAll((k, v) -> v * factor);
    }

    private void clampPosition(Map<OptimizationParameter, Double> pos) {
        pos.replaceAll((k, v) -> Math.min(Config.INSTANCE.parameterRanges.get(k).getMax(), Math.max(v, Config.INSTANCE.parameterRanges.get(k).getMin())));
    }

    private void updateCoordinates() {
        parameters.replaceAll((k, v) -> clamp(Config.INSTANCE.parameterRanges.get(k), v + velocity.get(k)));
        clampPosition(parameters);
        logger.trace("Neue Partikelposition: %s", parameters.toString());
    }

    private double clamp(ParameterRange<Double> range, double value) {
        return Double.max(range.getMin(), Double.min(value, range.getMax()));
    }

    private void updatePersonalBest() {
        ArtificialBeeColonyParameters parameters = new ArtificialBeeColonyParameters(
                0L, 0, 0, 0, new HashMap<>());

        for (OptimizationParameter param : this.parameters.keySet()) {
            parameters = param.getParameterApplyFunction().applyValue(parameters, this.parameters.get(param));
        }

        double score = testParameters(parameters);

        if (score < personalBest) {
            logger.debug("Neue (lokal) beste Parameter: (Score %.1f): %s", score, parameters);
            personalBest = score;
            personalBestParameters = new HashMap<>(this.parameters);
            sharedState.checkUpdateGBest(score, new HashMap<>(this.parameters));
        }
    }

    private double testParameters(ArtificialBeeColonyParameters parameters) {
        logger.info("Teste Parameter %s", parameters);
        double avg = 0.0;

        for (int i = 0 ; i < Config.INSTANCE.sampleSizePerConfiguration ; i++) {
            logger.debug("Testlauf #%d", i + 1);
            Logger.setLogLevel(Config.INSTANCE.executionLogLevel);
            ArtificialBeeColonyOptimization abc = new ArtificialBeeColonyOptimization(sharedState.getCities(), parameters);
            Route r = abc.findOptimalRoute();
            Logger.setLogLevel(Config.INSTANCE.logLevel);
            logger.trace("Gefundene Route (Länge %.1f): %s", r.getTotalDistance(), r);
            avg += r.getTotalDistance();
        }

        return avg / Config.INSTANCE.sampleSizePerConfiguration;
    }

    public double getPersonalBest() {
        return personalBest;
    }
}
