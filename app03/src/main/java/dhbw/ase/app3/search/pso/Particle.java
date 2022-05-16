package dhbw.ase.app3.search.pso;

import dhbw.ase.log.Logger;
import dhbw.ase.random.MersenneTwisterFast;
import dhbw.ase.tsp.Route;
import dhbw.ase.tsp.Transpositions;

public class Particle {
    private final static Logger logger = Logger.getLogger(Particle.class);
    ParticleSwarmOptimization sharedState;
    MersenneTwisterFast rng;

    double personalBest = Double.MAX_VALUE;
    Route personalBestRoute;

    Route coordinates;
    Transpositions velocity;

    public Particle(ParticleSwarmOptimization pso) {
        sharedState = pso;
        rng = new MersenneTwisterFast(System.nanoTime());
    }

    public void initialize() {
        coordinates = new Route(sharedState.getCities()).shuffled();
        velocity = coordinates.difference(coordinates.shuffled());

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
        PsoParameters p = sharedState.getParameters();

        double cognitiveFactor = p.getCognitiveRatio() * rng.nextDouble();
        Transpositions cognitiveVelocity = personalBestRoute.difference(coordinates);

        double socialFactor = p.getSocialRatio() * rng.nextDouble();
        Transpositions socialVelocity = sharedState.getGlobalBestRoute().difference(coordinates);

        velocity = velocity.times(p.getInertia())
                .add(cognitiveVelocity.times(cognitiveFactor))
                .add(socialVelocity.times(socialFactor));
    }

    private void updateCoordinates() {
        coordinates = velocity.addTo(coordinates);
    }

    private void updatePersonalBest() {
        Route route = coordinates;
        double score = route.getTotalDistance();

        if (score < personalBest) {
            logger.debug("Neue (lokal) beste Route: (Länge %01.4f): %s", score, route);
            personalBest = score;
            personalBestRoute = route;
            sharedState.checkUpdateGBest(score, route);
        }
    }
}
