import java.util.List;
import java.util.stream.Collectors;

import dhbw.ase.app2.Config;
import dhbw.ase.app2.abc.ArtificialBeeColonyOptimization;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;
import dhbw.ase.util.ILoader;
import dhbw.ase.util.loader.Dataset;
import dhbw.ase.util.loader.LoaderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RequirementTest {
    private static final Logger logger = Logger.getLogger(RequirementTest.class);

    public static List<City> loadData(Dataset ds) {
        logger.debug("Lade Datenset: " + ds);
        ILoader loader = new LoaderImpl();
        List<City> cities = loader.loadDataset(ds);

        logger.debug("Geladene Städte:\n" + cities.stream().map(city -> "    " + city).collect(Collectors.joining("\n")));
        return cities;
    }

    @BeforeAll
    static void setLogLevel() {
        Logger.setLogLevel(Config.INSTANCE.logLevel);
    }

    @Test
    public void testQualityMet() {
        Dataset dataset = Dataset.A280;
        double knownOptimum = 2579.0;
        double maxDeviation = 0.05;

        List<City> data = loadData(dataset);
        ArtificialBeeColonyOptimization abc = new ArtificialBeeColonyOptimization(
                data, Config.INSTANCE.defaultAlgorithmParameters);
        Route optimalRoute = abc.findOptimalRoute();

        logger.system("Route mit der Länge %.1f wurde gefunden: %s", optimalRoute.getTotalDistance(), optimalRoute);
        double maxAllowedValue = knownOptimum * (1.0 + maxDeviation);
        Assertions.assertTrue(optimalRoute.getTotalDistance() < maxAllowedValue, "Routenlänge erfüllt die Bedingung nicht.");
    }
}
