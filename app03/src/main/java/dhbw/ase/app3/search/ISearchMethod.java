package dhbw.ase.app3.search;

import java.util.List;

import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app3.Config;
import dhbw.ase.app3.search.pso.ParticleSwarmOptimization;
import dhbw.ase.tsp.City;

public interface ISearchMethod {
    ArtificialBeeColonyParameters searchBestParameters();

    static ISearchMethod getConfiguredInstance(List<City> cities) {
        return switch (Config.INSTANCE.searchMethod) {
            case BINARY -> null; // TODO
            case BRUTE_FORCE -> new BruteForceSearchMethod(cities);
            case PARTICLE_SWARM -> new PsoSearchMethod(cities); // TODO
        };
    }
}
