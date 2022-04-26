package dhbw.ase.tsp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dhbw.ase.random.MersenneTwisterFast;

public class Transpositions {
    private List<Transposition> transpositions = new ArrayList<>();

    public Transpositions(List<Transposition> transpositions) {
        this.transpositions.addAll(transpositions);
    }

    public static Transpositions randomSwaps(List<City> cityOrder, int revisitExplorationDeviation) {
        MersenneTwisterFast random = new MersenneTwisterFast(System.nanoTime());
        List<Transposition> toReturn = new LinkedList<>();

        for (int i = 0 ; i < revisitExplorationDeviation ; i++) {
            City c1 = cityOrder.get(random.nextInt(cityOrder.size()));
            City c2 = cityOrder.get(random.nextInt(cityOrder.size()));

            while (c1 == c2) {
                c1 = cityOrder.get(random.nextInt(cityOrder.size()));
                c2 = cityOrder.get(random.nextInt(cityOrder.size()));
            }

            toReturn.add(new Transposition(c1, c2));
        }

        return new Transpositions(toReturn);
    }

    public Route applyTo(Route r) {
        List<City> cities = new ArrayList<>(r.getCityOrder());

        for (Transposition transposition : transpositions) {
            transposition.applyTo(cities);
        }

        return new Route(cities);
    }

    public Transpositions times(double factor) {
        // Check: 0 <= factor <= 1
        if (factor < 0 || factor > 1) {
            throw new IllegalArgumentException();
        }

        // If factor 0 -> no transpositions
        if (factor == 0) {
            return new Transpositions(new LinkedList<>());
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
