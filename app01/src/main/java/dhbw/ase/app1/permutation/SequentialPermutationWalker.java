package dhbw.ase.app1.permutation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;

/**
 * Implementiert einen Generator für alle Permutationen in sequentieller Reihenfolge (= klassisches Brute-Force).
 * Sobald alle Permutationen zurückgegeben wurden, wirft {@link IPermutationWalker#nextPermutation()} eine Exception.
 * <p>
 * Iterationsalgorithmus abgewandelt von: <a href="https://www.baeldung.com/java-array-permutations">https://www.baeldung.com/java-array-permutations</a>
 */
public class SequentialPermutationWalker implements IPermutationWalker {

    private final List<City> cities;

    // Numeric values for generating the permutations
    private final int arraySize;
    private final int[] indexes;
    private int currentIndex = -1;

    public SequentialPermutationWalker(List<City> cities) {
        // Copy List to avoid changes of the original
        this.cities = new ArrayList<>(cities);
        arraySize = cities.size();
        // Array is already initialized with 0
        indexes = new int[arraySize];
    }

    @Override
    public synchronized Route nextPermutation() {
        // Do not permute for the first request
        if (currentIndex == -1) {
            currentIndex++;
            return new Route(cities);
        }

        if (currentIndex >= arraySize) {
            throw new RuntimeException("All permutations were returned");
        }

        Route toReturn = null;
        while (toReturn == null) {
            if (indexes[currentIndex] < currentIndex) {
                Collections.swap(cities, currentIndex % 2 == 0 ? 0 : indexes[currentIndex], currentIndex);
                toReturn = new Route(cities);
                indexes[currentIndex]++;
                currentIndex = 0;
            } else {
                indexes[currentIndex] = 0;
                currentIndex++;
            }
        }

        return toReturn;
    }
}
