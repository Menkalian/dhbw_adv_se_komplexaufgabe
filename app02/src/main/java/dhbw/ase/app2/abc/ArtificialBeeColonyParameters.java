package dhbw.ase.app2.abc;

import dhbw.ase.json.SerializationHelper;

public class ArtificialBeeColonyParameters {
    private final SerializationHelper<ArtificialBeeColonyParameters> serializationHelper;

    private final long maxIterations;

    private final int foodSourceCount;
    private final int onlookerBeeCount;

    private final int revisitLimit;
    private final int revisitExplorationRadius;

    public ArtificialBeeColonyParameters(long maxIterations, int foodSourceCount, int onlookerBeeCount, int revisitLimit, int revisitExplorationRadius) {
        this.serializationHelper = new SerializationHelper<>(ArtificialBeeColonyParameters.class);

        this.maxIterations = maxIterations;
        this.foodSourceCount = foodSourceCount;
        this.onlookerBeeCount = onlookerBeeCount;
        this.revisitLimit = revisitLimit;
        this.revisitExplorationRadius = revisitExplorationRadius;
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

    public int getRevisitExplorationRadius() {
        return revisitExplorationRadius;
    }

    public String toString() {
        return serializationHelper.serialize(this);
    }
}
