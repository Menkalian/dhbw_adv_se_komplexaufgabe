package dhbw.ase.app3.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dhbw.ase.app2.abc.ArtificialBeeColonyOptimization;
import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app3.Config;
import dhbw.ase.app3.OptimizationParameter;
import dhbw.ase.app3.ParameterRange;
import dhbw.ase.log.Logger;
import dhbw.ase.tsp.City;
import dhbw.ase.tsp.Route;

public class BruteForceSearchMethod implements ISearchMethod {
    private static final Logger logger = Logger.getLogger(BruteForceSearchMethod.class);
    private final List<City> cities;
    private double record = Double.MAX_VALUE;
    private ArtificialBeeColonyParameters recordParameters = null;

    public BruteForceSearchMethod(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public ArtificialBeeColonyParameters searchBestParameters() {
        ArtificialBeeColonyParameters emptyParameters = new ArtificialBeeColonyParameters(
                0L, 0, 0, 0, new HashMap<>()
        );

        searchParameters(emptyParameters, Config.INSTANCE.parameterRanges);
        return this.recordParameters;
    }

    private void searchParameters(ArtificialBeeColonyParameters setParameters, Map<OptimizationParameter, ParameterRange<Double>> parametersToSearch) {
        if (parametersToSearch.isEmpty()) {
            double score = testParameters(setParameters);
            if (score < record) {
                logger.info("Neue beste Parameter gefunden: %s", setParameters);
                record = score;
                recordParameters = setParameters;
            }
            return;
        }

        Map<OptimizationParameter, ParameterRange<Double>> remainingParameters = new HashMap<>(parametersToSearch);
        OptimizationParameter selectedParameter = remainingParameters.keySet().stream().findAny().get();
        ParameterRange<Double> optimizationParameter = remainingParameters.remove(selectedParameter);

        for (Double value : optimizationParameter) {
            ArtificialBeeColonyParameters parameters = selectedParameter.getParameterApplyFunction().applyValue(setParameters, value);
            searchParameters(parameters, remainingParameters);
        }
    }

    private double testParameters(ArtificialBeeColonyParameters parameters) {
        logger.info("Teste Parameter %s", parameters);
        double avg = 0.0;

        for (int i = 0 ; i < Config.INSTANCE.sampleSizePerConfiguration ; i++) {
            logger.debug("Testlauf #%d", i + 1);
            Logger.setLogLevel(Config.INSTANCE.executionLogLevel);
            ArtificialBeeColonyOptimization abc = new ArtificialBeeColonyOptimization(cities, parameters);
            Route r = abc.findOptimalRoute();
            Logger.setLogLevel(Config.INSTANCE.logLevel);
            logger.trace("Gefundene Route (Länge %.1f): %s", r.getTotalDistance(), r);
            avg += r.getTotalDistance();
        }

        return avg / Config.INSTANCE.sampleSizePerConfiguration;
    }
}
