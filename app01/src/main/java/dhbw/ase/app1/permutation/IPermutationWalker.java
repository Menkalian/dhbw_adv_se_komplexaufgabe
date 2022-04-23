package dhbw.ase.app1.permutation;

import java.util.List;

import dhbw.ase.app1.Config;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;

public interface IPermutationWalker {
    Route nextPermutation();

    static IPermutationWalker getConfiguredImplementation(List<City> cities) {
        return switch (Config.INSTANCE.permutationMode) {
            case RANDOMIZED -> new RandomPermutationWalker(cities);
            case SEQUENTIAL -> new SequentialPermutationWalker(cities);
        };
    }
}
