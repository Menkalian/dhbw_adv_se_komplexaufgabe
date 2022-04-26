package dhbw.ase.app3;

import java.util.List;
import java.util.stream.Collectors;

import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app3.search.ISearchMethod;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.util.ILoader;
import dhbw.ase.util.loader.Dataset;
import dhbw.ase.util.loader.LoaderImpl;

public class App {
    private static final Logger logger = Logger.getLogger(dhbw.ase.app2.App.class);

    public static void main(String[] args) {
        logger.system("Komplexaufgabe App 03 - Start");
        Logger.setLogLevel(Config.INSTANCE.logLevel);

        List<City> cities = loadData();
        ISearchMethod method = ISearchMethod.getConfiguredInstance(cities);

        logger.system("Starte Parametersuche...");
        ArtificialBeeColonyParameters bestParameters = method.searchBestParameters();
        logger.system("Gefundene Parameter: %s", bestParameters);
        // TODO: Save to Json

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
