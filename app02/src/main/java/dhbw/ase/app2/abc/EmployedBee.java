package dhbw.ase.app2.abc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import dhbw.ase.log.LogLevel;
import dhbw.ase.log.Logger;
import dhbw.ase.random.MersenneTwisterFast;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;

public class EmployedBee {
    private static final AtomicLong instanceCount = new AtomicLong(0);

    private final Logger logger = Logger.getLogger(this);

    private final MersenneTwisterFast rng;
    private final Object currentPositionMutex = new Object();

    private final ArtificialBeeColonyOptimization sharedState;
    private final ArtificialBeeColonyParameters parameters;
    private final List<NeighbourFindingMethod> methodSelection;
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

        methodSelection = new ArrayList<>();
        parameters
                .getNeighbourFindingMethodRatio()
                .forEach((method, amount) -> {
                    for (int i = 0 ; i < amount ; i++) {
                        methodSelection.add(method);
                    }
                });
        if (methodSelection.isEmpty()) {
            methodSelection.add(NeighbourFindingMethod.NOOP);
        }
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
        NeighbourFindingMethod method;
        synchronized (rng) {
            method = methodSelection.get(rng.nextInt(methodSelection.size()));
        }

        logger.debug("Erkundung durch %s mit %s", isOnlooker ? "Onlooker Bee" : "Employed Bee", method);
        logPosition(false);
        visitCount.incrementAndGet();

        Route current = currentPosition;
        Route explorationPoint;

        switch (method) {
            case POINT_SWAP: {
                int i1;
                int i2;

                synchronized (rng) {
                    int size = currentPosition.getCityOrder().size();
                    i1 = rng.nextInt(size);
                    i2 = rng.nextInt(size);
                    while (i1 == i2) {
                        i1 = rng.nextInt(size);
                        i2 = rng.nextInt(size);
                    }
                }

                explorationPoint = current.swapped(i1, i2);
                break;
            }
            case BLOCK_SWAP: {
                int startPoint;
                int goalPoint;
                int length;

                synchronized (rng) {
                    int size = currentPosition.getCityOrder().size();
                    int startIdx1 = rng.nextInt(size);
                    int startIdx2 = rng.nextInt(size);
                    int goalIdx1 = rng.nextInt(size);
                    int goalIdx2 = rng.nextInt(size);

                    startPoint = Math.min(startIdx1, startIdx2);
                    goalPoint = Math.min(goalIdx1, goalIdx2);
                    length = Math.min(Math.abs(startIdx1 - startIdx2), Math.abs(goalIdx1 - goalIdx2));
                }

                explorationPoint = current.blockSwap(startPoint, goalPoint, length);
                break;
            }
            case PARTIAL_SHIFT: {
                int startPoint;
                int goalPoint;
                int length;

                synchronized (rng) {
                    int size = currentPosition.getCityOrder().size();
                    int startIdx1 = rng.nextInt(size);
                    int startIdx2 = rng.nextInt(size);
                    int goalIdx1 = rng.nextInt(size);
                    int goalIdx2 = rng.nextInt(size);

                    startPoint = Math.min(startIdx1, startIdx2);
                    goalPoint = Math.min(goalIdx1, goalIdx2);
                    length = Math.min(Math.abs(startIdx1 - startIdx2), Math.abs(goalIdx1 - goalIdx2));
                }

                explorationPoint = current.partialShift(startPoint, goalPoint, length);
                break;
            }
            case SINGLE_SHIFT: {
                int startPoint;
                int goalPoint;

                synchronized (rng) {
                    int size = currentPosition.getCityOrder().size();
                    startPoint = rng.nextInt(size);
                    goalPoint = rng.nextInt(size);
                }

                explorationPoint = current.partialShift(startPoint, goalPoint, 1);
                break;
            }
            case WEIGHTED_SINGLE_SHIFT: {
                int startPoint;
                synchronized (rng) {
                    startPoint = rng.nextInt(currentPosition.getCityOrder().size());
                }

                City c2 = Route.getDistanceHelper().weightedNeighborSelection(current.getCityOrder().get(startPoint));
                int goalPoint = current.getCityOrder().indexOf(c2);

                // Shift in front of the selected city
                explorationPoint = current.partialShift(startPoint, goalPoint, 1);
                break;
            }
            case PARTIAL_REVERSE: {
                int startPoint;
                int length;
                synchronized (rng) {
                    int size = currentPosition.getCityOrder().size();
                    int i1 = rng.nextInt(size);
                    int i2 = rng.nextInt(size);
                    startPoint = Math.min(i1, i2);
                    length = Math.abs(i1 - i2);
                }

                explorationPoint = current.partialReverse(startPoint, length);
                break;
            }
            default: {
                explorationPoint = current;
                break;
            }
        }

        double score = explorationPoint.getTotalDistance();
        logger.trace("Erkunde Punkt (Länge: %.1f): %s", score, explorationPoint);

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
        logger.log(isNew ? LogLevel.DEBUG : LogLevel.TRACE, "%s Position (Score: %.1f): %s", isNew ? "Neue" : "Aktuelle", currentScore, currentPosition);
    }
}
