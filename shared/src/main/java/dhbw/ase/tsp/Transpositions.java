package dhbw.ase.tsp;

import java.util.LinkedList;
import java.util.List;

public class Transpositions {
    private List<Transposition> transpositions = new LinkedList<>();

    public Transpositions(List<Transposition> transpositions) {
        this.transpositions.addAll(transpositions);
    }

    public Route addTo(Route r) {
        Route toReturn = r;
        for (Transposition transposition : transpositions) {
            toReturn = transposition.addTo(toReturn);
        }
        return toReturn;
    }

    public Transpositions times(double factor) {
        // If factor 0 -> no transpositions
        if (factor == 0)
            return new Transpositions(new LinkedList<>());
        // Check: 0 <= factor <= 1
        if (factor < 0 || factor > 1) {
            throw new IllegalArgumentException();
        }
        // else take slice of transpositions (e.g. 10 transpositions * 0.6 = first 6 transpositions
        // always return a new instance
        return new Transpositions(transpositions.subList(0, (int) Math.round(factor * transpositions.size())));
    }

    public Transpositions add(Transpositions other) {
        List<Transposition> added = new LinkedList<>(transpositions);
        added.addAll(other.transpositions);
        return new Transpositions(added);
    }
}
