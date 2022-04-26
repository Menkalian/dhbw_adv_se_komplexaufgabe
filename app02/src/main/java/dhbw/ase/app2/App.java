package dhbw.ase.app2;

import java.util.List;
import java.util.stream.Collectors;

import dhbw.ase.app2.abc.ArtificialBeeColonyOptimization;
import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;
import dhbw.ase.util.ILoader;
import dhbw.ase.util.loader.Dataset;
import dhbw.ase.util.loader.LoaderImpl;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        logger.system("Komplexaufgabe App 02 - Start");
        Logger.setLogLevel(Config.INSTANCE.logLevel);

        // TODO: Load Json here
        ArtificialBeeColonyParameters abcParameters = Config.INSTANCE.defaultAlgorithmParameters;
        List<City> data = loadData();

        ArtificialBeeColonyOptimization abc = new ArtificialBeeColonyOptimization(data, abcParameters);
        Route optimalRoute = abc.findOptimalRoute();

        logger.system("Route mit der Länge %.4f wurde gefunden: %s", optimalRoute.getTotalDistance(), optimalRoute);
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
