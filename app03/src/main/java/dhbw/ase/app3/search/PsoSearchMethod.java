package dhbw.ase.app3.search;

import java.util.List;

import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app3.Config;
import dhbw.ase.app3.search.pso.ParticleSwarmOptimization;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;

public class PsoSearchMethod implements ISearchMethod {
    /**
     * Used algorithm: PSO
     * <p>
     * Some inspiration for applying the algorithm to the TSP taken from:
     * <a href="http://wwwmayr.informatik.tu-muenchen.de/konferenzen/Ferienakademie14/literature/HMHW11.pdf">http://wwwmayr.informatik.tu-muenchen.de/konferenzen/Ferienakademie14/literature/HMHW11.pdf</a>
     */

    private static final Logger logger = Logger.getLogger(PsoSearchMethod.class);
    private final List<City> cities;

    public PsoSearchMethod(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public ArtificialBeeColonyParameters searchBestParameters() {
        ParticleSwarmOptimization optimization = new ParticleSwarmOptimization(Config.INSTANCE.particleSwarmParameters, cities);
        return optimization.findOptimalParameters();
    }
}