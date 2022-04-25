package dhbw.ase.app2;

import dhbw.ase.app2.pso.ParticleSwarmOptimization;
import dhbw.ase.app2.pso.PsoParameters;
import dhbw.ase.log.LogLevel;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;
import dhbw.ase.util.ILoader;
import dhbw.ase.util.loader.Dataset;
import dhbw.ase.util.loader.LoaderImpl;

import java.util.List;
import java.util.stream.Collectors;

public class App {
    /**
     * Used algorithm: PSO
     * <p>
     * Some inspiration for applying the algorithm to the TSP taken from:
     * <a href="http://wwwmayr.informatik.tu-muenchen.de/konferenzen/Ferienakademie14/literature/HMHW11.pdf">http://wwwmayr.informatik.tu-muenchen.de/konferenzen/Ferienakademie14/literature/HMHW11.pdf</a>
     */

    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        Logger.setLogLevel(LogLevel.TRACE);
        logger.system("Komplexaufgabe App 02 - Start");
        System.out.println("Advanced Software Engineering");
        PsoParameters psoParameters = new PsoParameters(
                10000,
                20,
                0.4,
                0.6,
                0.6);
        List<City> data = loadData();

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization(data, psoParameters);
        Route optimalRoute = pso.findOptimalRoute();
        System.out.println(optimalRoute);
        System.out.println(optimalRoute.getTotalDistance());

        Logger.shutdown();
    }


    public static List<City> loadData() {
        Dataset ds = Dataset.A280;

        logger.debug("Lade Datenset: " + ds);
        ILoader loader = new LoaderImpl();
        List<City> cities = loader.loadDataset(ds);

        logger.debug("Geladene Städte:\n" + cities.stream().map(city -> "    " + city).collect(Collectors.joining("\n")));
        return cities;
    }
}
