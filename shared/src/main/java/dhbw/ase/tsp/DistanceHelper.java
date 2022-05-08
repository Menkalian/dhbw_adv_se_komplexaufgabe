package dhbw.ase.tsp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import dhbw.ase.random.MersenneTwisterFast;

/**
 * This class is used to cache the result of some time-intensive deterministic calculations.
 * It is not used for saving the distances between cities, since reading from the cache is slower than the calculation (or at best not noticeably faster)
 */
public class DistanceHelper {
    private final Map<City, Map<Double, City>> weightedSelections;

    DistanceHelper() {
        weightedSelections = new HashMap<>();
    }

    public void precalculateCities(List<City> cities) {
        weightedSelections.clear();

        for (City keyCity : cities) {
            double sumOfWeights = cities
                    .stream()
                    .filter(c -> c != keyCity) // A reference check is sufficient, since these cities come from the same list
                    .map(keyCity::distance)
                    .mapToDouble(d -> 1.0 / (d + 0.01)) // Ensure we are not dividing by 0
                    .sum();

            AtomicReference<Double> cumWeight = new AtomicReference<>(0.0);
            Map<Double, City> weightedSelectionMap = new HashMap<>(cities.size() - 1);
            cities.stream()
                  .filter(c -> c != keyCity) // A reference check is sufficient, since these cities come from the same list
                  .forEach(c -> {
                      double weightedKey = cumWeight.updateAndGet(v -> v + (1.0 / (keyCity.distance(c) + 0.01)) / sumOfWeights);
                      weightedSelectionMap.put(weightedKey, c);
                  });

            weightedSelections.put(keyCity, weightedSelectionMap);
        }
    }

    public synchronized City weightedNeighborSelection(City c) {
        MersenneTwisterFast rng = new MersenneTwisterFast(System.nanoTime());
        double r = rng.nextDouble();
        Map<Double, City> weightedSelection = weightedSelections.get(c);
        double key = weightedSelection
                .keySet()
                .stream()
                .filter(d -> d < r)
                .max(Double::compareTo)
                // If no valid key is found we have hit an edge case caused by rounding errors,
                // so we just use any key. This is not a big inaccuracy.
                .orElse(weightedSelection.keySet().stream().findAny().get());
        return weightedSelection.get(key);
    }
}
