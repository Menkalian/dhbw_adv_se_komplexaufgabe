package dhbw.ase.app1;

import java.util.List;
import java.util.stream.Collectors;

import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;
import dhbw.ase.util.ILoader;
import dhbw.ase.util.loader.Dataset;
import dhbw.ase.util.loader.LoaderImpl;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        logger.system("Komplexaufgabe App 01 - Start");
        Logger.setLogLevel(Config.INSTANCE.logLevel);

        List<City> data = loadData();
        Route.getDistanceHelper().precalculateCities(data);

        BruteForceOptimization bfs = new BruteForceOptimization(data);
        Route best = bfs.searchOptimalRoute();

        logger.system("Gefundene Route (Länge: " + best.getTotalDistance() + ": " + best);
    }

    public static List<City> loadData() {
        Dataset ds = Config.INSTANCE.dataset;

        logger.debug("Lade Datenset: " + ds);
        ILoader loader = new LoaderImpl();
        List<City> cities = loader.loadDataset(ds);

        logger.debug("Geladene Städte:\n" + cities.stream().map(city -> "    " + city).collect(Collectors.joining("\n")));
        return cities;
    }
}
