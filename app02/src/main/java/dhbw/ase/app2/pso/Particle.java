package dhbw.ase.app2.pso;

import dhbw.ase.random.MersenneTwisterFast;
import dhbw.ase.tsp.Route;
import dhbw.ase.tsp.Transpositions;

public class Particle {
    ParticleSwarmOptimization sharedState;
    MersenneTwisterFast rng;

    double personalBest;
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
            personalBest = score;
            personalBestRoute = route;
            sharedState.checkUpdateGBest(score, route);
        }
    }
}
