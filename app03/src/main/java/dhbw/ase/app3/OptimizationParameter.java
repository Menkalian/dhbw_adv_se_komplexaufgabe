package dhbw.ase.app3;

import dhbw.ase.app2.abc.ArtificialBeeColonyParameters;
import dhbw.ase.app2.abc.NeighbourFindingMethod;

public enum OptimizationParameter {
    ITERATION((params, value) -> params.withMaxIterations(value.longValue())),
    FOOD_SOURCES((params, value) -> params.withFoodSourceCount(value.intValue())),
    ONLOOKER_BEES((params, value) -> params.withOnlookerBeeCount(value.intValue())),
    REVISIT_LIMIT((params, value) -> params.withRevisitLimit(value.intValue())),
    POINT_SWAP_WEIGHT((params, value) -> params.withNeighborFindingMethodWeight(NeighbourFindingMethod.POINT_SWAP, value.intValue())),
    BLOCK_SWAP_WEIGHT((params, value) -> params.withNeighborFindingMethodWeight(NeighbourFindingMethod.BLOCK_SWAP, value.intValue())),
    SINGLE_SHIFT_WEIGHT((params, value) -> params.withNeighborFindingMethodWeight(NeighbourFindingMethod.SINGLE_SHIFT, value.intValue())),
    WEIGHTED_SINGLE_SHIFT_WEIGHT((params, value) -> params.withNeighborFindingMethodWeight(NeighbourFindingMethod.WEIGHTED_SINGLE_SHIFT, value.intValue())),
    PARTIAL_REVERSE_WEIGHT((params, value) -> params.withNeighborFindingMethodWeight(NeighbourFindingMethod.PARTIAL_REVERSE, value.intValue())),
    PARTIAL_SHIFT_WEIGHT((params, value) -> params.withNeighborFindingMethodWeight(NeighbourFindingMethod.PARTIAL_SHIFT, value.intValue())),
    ;

    private final ParameterApplyFunction applyValueFunction;

    OptimizationParameter(ParameterApplyFunction applyValue) {
        applyValueFunction = applyValue;
    }

    public ParameterApplyFunction getParameterApplyFunction() {
        return applyValueFunction;
    }

    @FunctionalInterface
    public interface ParameterApplyFunction {
        ArtificialBeeColonyParameters applyValue(ArtificialBeeColonyParameters previous, Double value);
    }
}
