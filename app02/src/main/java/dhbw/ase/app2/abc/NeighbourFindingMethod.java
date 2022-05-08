package dhbw.ase.app2.abc;

public enum NeighbourFindingMethod {
    // No change to the route is done (fallback)
    NOOP,
    // Two random points are selected and swapped
    POINT_SWAP,
    // Two random Blocks of points are swapped
    BLOCK_SWAP,
    // A random part of the route is reversed (e.g. a-b-[c-d-e]-f => a-b-[e-d-c]-f)
    PARTIAL_REVERSE,
    // A random part of the route is shifted to a different position
    PARTIAL_SHIFT,
    // A random single point is shifted to a different position
    SINGLE_SHIFT,
    // Taken from this paper: http://www.lnit.org/uploadfile/2013/0603/20130603040519185.pdf
    // A random city is selected and another city is selected based on the distance. Then the first city is shifted to be in front of the second city in the route
    WEIGHTED_SINGLE_SHIFT
}
