package dhbw.ase.app2.abc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public ArtificialBeeColonyParameters withMaxIterations(long maxIterations) {
        return new ArtificialBeeColonyParameters(
                maxIterations,
                this.foodSourceCount,
                this.onlookerBeeCount,
                this.revisitLimit,
                new HashMap<>(this.neighbourFindingMethodRatio)
        );
    }

    public ArtificialBeeColonyParameters withFoodSourceCount(int foodSourceCount) {
        return new ArtificialBeeColonyParameters(
                this.maxIterations,
                foodSourceCount,
                this.onlookerBeeCount,
                this.revisitLimit,
                new HashMap<>(this.neighbourFindingMethodRatio)
        );
    }

    public ArtificialBeeColonyParameters withOnlookerBeeCount(int onlookerBeeCount) {
        return new ArtificialBeeColonyParameters(
                this.maxIterations,
                this.foodSourceCount,
                onlookerBeeCount,
                this.revisitLimit,
                new HashMap<>(this.neighbourFindingMethodRatio)
        );
    }

    public ArtificialBeeColonyParameters withRevisitLimit(int revisitLimit) {
        return new ArtificialBeeColonyParameters(
                this.maxIterations,
                this.foodSourceCount,
                this.onlookerBeeCount,
                revisitLimit,
                new HashMap<>(this.neighbourFindingMethodRatio)
        );
    }

    public ArtificialBeeColonyParameters withNeighborFindingMethodWeight(NeighbourFindingMethod method, int weight) {
        Map<NeighbourFindingMethod, Integer> newNeighborFindingMethodRatio = new HashMap<>(this.neighbourFindingMethodRatio);
        newNeighborFindingMethodRatio.put(method, weight);
        return new ArtificialBeeColonyParameters(
                this.maxIterations,
                this.foodSourceCount,
                this.onlookerBeeCount,
                this.revisitLimit,
                newNeighborFindingMethodRatio
        );
    }

    public String toString() {
        // If the object was loaded from a JSON, then serializationHelper is null
        return Objects
                .requireNonNullElseGet(serializationHelper, () -> new SerializationHelper<>(ArtificialBeeColonyParameters.class))
                .serialize(this);
    }
}
