package dhbw.ase.app1.permutation;

import java.util.List;

import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;

/**
 * Implementiert einen Generator für zufällige Permutationen (=Zufallssuche).
 * Es ist nicht garantiert, dass keine doppelten Permutationen ausgegeben werden, aber bei einer ausreichend großen Menge ist die Wahrscheinlichkeit dafür gering.
 */
public class RandomPermutationWalker implements IPermutationWalker {

    private final Route baseRoute;

    public RandomPermutationWalker(List<City> cities) {
        this.baseRoute = new Route(cities);
    }

    @Override
    public Route nextPermutation() {
        return baseRoute.shuffled();
    }
}
