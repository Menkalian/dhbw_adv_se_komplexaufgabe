package dhbw.ase.app3.search;

import java.util.List;

import dhbw.ase.app2.abc.ArtificialBeeColonyOptimization;
import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app3.Config;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;

public class BruteForceSearchMethod implements ISearchMethod {
    private static final Logger logger = Logger.getLogger(BruteForceSearchMethod.class);
    private final List<City> cities;

    public BruteForceSearchMethod(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public ArtificialBeeColonyParameters searchBestParameters() {
        double record = Double.MAX_VALUE;
        ArtificialBeeColonyParameters recordParams = null;

        for (int iteration : Config.INSTANCE.iterationRange) {
            for (int foodSources : Config.INSTANCE.foodSourceRange) {
                for (int onlookers : Config.INSTANCE.onlookerBeeRange) {
                    for (int revisitLimit : Config.INSTANCE.revisitLimitRange) {
                        for (int revisitExploration : Config.INSTANCE.revisitExplorationRadius) {
                            ArtificialBeeColonyParameters params = new ArtificialBeeColonyParameters(
                                    iteration,
                                    foodSources,
                                    onlookers,
                                    revisitLimit,
                                    revisitExploration);
                            double score = testParameters(params);
                            if (score < record) {
                                logger.info("Neue beste Parameter gefunden: %s", params);
                                record = score;
                                recordParams = params;
                            }
                        }
                    }
                }
            }
        }

        return recordParams;
    }

    private double testParameters(ArtificialBeeColonyParameters parameters) {
        logger.info("Teste Parameter %s", parameters);
        double avg = 0.0;

        for (int i = 0 ; i < Config.INSTANCE.sampleSizePerConfiguration ; i++) {
            logger.debug("Testlauf #%d", i + 1);
            ArtificialBeeColonyOptimization abc = new ArtificialBeeColonyOptimization(cities, parameters);
            Route r = abc.findOptimalRoute();
            logger.trace("Gefundene Route (Länge %.4f): %s", r.getTotalDistance(), r);
            avg += r.getTotalDistance();
        }

        return avg / Config.INSTANCE.sampleSizePerConfiguration;
    }
}
