package dhbw.ase.app2.abc;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import dhbw.ase.log.LogLevel;
import dhbw.ase.log.Logger;
import dhbw.ase.random.MersenneTwisterFast;
import dhbw.ase.tsp.Route;
import dhbw.ase.tsp.Transpositions;

public class EmployedBee {
    private static final AtomicLong instanceCount = new AtomicLong(0);

    private final Logger logger = Logger.getLogger(this);

    private final MersenneTwisterFast rng;
    private final Object currentPositionMutex = new Object();

    private final ArtificialBeeColonyOptimization sharedState;
    private final ArtificialBeeColonyParameters parameters;
    private final long id;

    private final AtomicInteger visitCount;

    private Route currentPosition;
    private double currentScore;

    public EmployedBee(ArtificialBeeColonyOptimization abc) {
        id = instanceCount.incrementAndGet();
        sharedState = abc;
        parameters = abc.getParameters();
        rng = new MersenneTwisterFast(System.nanoTime());

        currentPosition = null;
        visitCount = new AtomicInteger(0);
    }

    public double getCurrentScore() {
        return currentScore;
    }

    public void scoutIfNecessary() {
        if (visitCount.get() >= parameters.getRevisitLimit()) {
            logger.debug("Verlasse aktuelle Position");
            currentPosition = null;
        }

        if (currentPosition == null) {
            logger.debug("Suche neue Position");
            currentPosition = sharedState.getBaseRoute().shuffled();
            currentScore = currentPosition.getTotalDistance();
            visitCount.set(0);
            logPosition(true);

            sharedState.checkUpdateGlobalBest(currentScore, currentPosition);
        }
        sharedState.countDown();
    }

    public void explore(boolean isOnlooker) {
        logger.debug("Erkundung durch %s", isOnlooker ? "Onlooker Bee" : "Employed Bee");
        logPosition(false);
        visitCount.incrementAndGet();

        double r;
        // MersenneTwister implementation is not thread safe
        synchronized (rng) {
            r = rng.nextDouble();
        }

        Route current = currentPosition;
        Transpositions delta = Transpositions
                .randomSwaps(currentPosition.getCityOrder(), parameters.getRevisitExplorationRadius())
                .times(r);

        Route explorationPoint = delta.applyTo(current);
        double score = explorationPoint.getTotalDistance();

        synchronized (currentPositionMutex) {
            if (score < currentScore) {
                currentPosition = explorationPoint;
                currentScore = score;
                visitCount.set(0);
                logPosition(true);

                sharedState.checkUpdateGlobalBest(currentScore, currentPosition);
            }
        }

        sharedState.countDown();
    }

    public String toString() {
        return "EmployedBee(" + id + ")";
    }

    private void logPosition(boolean isNew) {
        logger.log(isNew ? LogLevel.DEBUG : LogLevel.TRACE, "%s Position (Score: %01.4f): %s", isNew ? "Neue" : "Aktuelle", currentScore, currentPosition);
    }
}
