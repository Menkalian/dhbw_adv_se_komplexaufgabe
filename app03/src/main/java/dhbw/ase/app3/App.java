package dhbw.ase.app3;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app3.search.ISearchMethod;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;
import dhbw.ase.util.ILoader;
import dhbw.ase.util.loader.Dataset;
import dhbw.ase.util.loader.LoaderImpl;

public class App {
    private static final Logger logger = Logger.getLogger(dhbw.ase.app2.App.class);

    public static void main(String[] args) {
        Logger.setConfig(Config.INSTANCE.logLevel,
                         Config.INSTANCE.consoleLogLevel,
                         Config.INSTANCE.fileLogLevel,
                         Config.INSTANCE.logFilePath);
        logger.system("Komplexaufgabe App 03 - Start");

        List<City> cities = loadData();
        Route.getDistanceHelper().precalculateCities(cities);
        ISearchMethod method = ISearchMethod.getConfiguredInstance(cities);

        logger.system("Starte Parametersuche...");
        ArtificialBeeColonyParameters bestParameters = method.searchBestParameters();
        logger.system("Gefundene Parameter: %s", bestParameters);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm");
        File jsonOut = new File("best_parameters-" + timeFormatter.format(LocalDateTime.now()) + ".json");

        try (PrintStream fout = new PrintStream(jsonOut)) {
            fout.println(bestParameters.toString());
        } catch (IOException ex) {
            logger.error("Fehler beim Schreiben der Parameterdatei %s: %s", jsonOut.getName(), ex.getMessage());
            ex.printStackTrace();
        }

        Logger.closeLogFile();
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
