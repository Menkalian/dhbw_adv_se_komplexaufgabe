package dhbw.ase.app3.search.pso;

import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.log.Logger;
import dhbw.ase.random.MersenneTwisterFast;
import dhbw.ase.tsp.Route;
import dhbw.ase.tsp.Transpositions;

public class Particle {
    private final static Logger logger = Logger.getLogger(Particle.class);
    ParticleSwarmOptimization sharedState;
    MersenneTwisterFast rng;

    double personalBest = Double.MAX_VALUE;
    ArtificialBeeColonyParameters personalBestParameters;

    ArtificialBeeColonyParameters parameters;
    Transpositions velocity;

    public Particle(ParticleSwarmOptimization pso) {
        sharedState = pso;
        rng = new MersenneTwisterFast(System.nanoTime());
    }

    public void initialize() {
        parameters = new Route(sharedState.getCities()).shuffled();
        velocity = parameters.difference(parameters.shuffled());

        updatePersonalBest();

        sharedState.countDown();
    }

    public void runIteration() {
        updateVelocity();
        updateCoordinates();
        updatePersonalBest();
        sharedState.countDown();
    }

    private void updateVelocity() {
        PsoParameters p = sharedState.getPsoParameters();

        double cognitiveFactor = p.getCognitiveRatio() * rng.nextDouble();
        Transpositions cognitiveVelocity = personalBestParameters.difference(parameters);

        double socialFactor = p.getSocialRatio() * rng.nextDouble();
        Transpositions socialVelocity = sharedState.getGlobalBestParameters().difference(parameters);

        velocity = velocity.times(p.getInertia())
                .add(cognitiveVelocity.times(cognitiveFactor))
                .add(socialVelocity.times(socialFactor));
    }

    private void updateCoordinates() {
        parameters = velocity.addTo(parameters);
    }

    private void updatePersonalBest() {
        Route route = parameters;
        double score = route.getTotalDistance();

        if (score < personalBest) {
            logger.debug("Neue (lokal) beste Route: (Länge %01.4f): %s", score, route);
            personalBest = score;
            personalBestParameters = route;
            sharedState.checkUpdateGBest(score, route);
        }
    }
}
