package dhbw.ase.app2.abc;

import java.util.Map;

import dhbw.ase.json.SerializationHelper;

public class ArtificialBeeColonyParameters {
    // transient = Do not serialize the SerializationHelper
    private final transient SerializationHelper<ArtificialBeeColonyParameters> serializationHelper;

    private final long maxIterations;

    private final int foodSourceCount;
    private final int onlookerBeeCount;

    private final int revisitLimit;
    private final Map<NeighbourFindingMethod, Integer> neighbourFindingMethodRatio;

    public ArtificialBeeColonyParameters(long maxIterations, int foodSourceCount, int onlookerBeeCount, int revisitLimit, Map<NeighbourFindingMethod, Integer> neighbourFindingMethodRatio) {
        this.serializationHelper = new SerializationHelper<>(ArtificialBeeColonyParameters.class);

        this.maxIterations = maxIterations;
        this.foodSourceCount = foodSourceCount;
        this.onlookerBeeCount = onlookerBeeCount;
        this.revisitLimit = revisitLimit;
        this.neighbourFindingMethodRatio = neighbourFindingMethodRatio;
    }

    public long getMaxIterations() {
        return maxIterations;
    }

    public int getFoodSourceCount() {
        return foodSourceCount;
    }

    public int getOnlookerBeeCount() {
        return onlookerBeeCount;
    }

    public int getRevisitLimit() {
        return revisitLimit;
    }

    public Map<NeighbourFindingMethod, Integer> getNeighbourFindingMethodRatio() {
        return neighbourFindingMethodRatio;
    }

    public String toString() {
        return serializationHelper.serialize(this);
    }
}
