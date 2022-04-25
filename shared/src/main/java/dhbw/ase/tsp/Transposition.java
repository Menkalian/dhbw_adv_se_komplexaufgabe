package dhbw.ase.tsp;

import java.util.Collections;
import java.util.LinkedList;

public class Transposition {
    private final City c1;
    private final City c2;

    public Transposition(City c1, City c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public Route addTo(Route r) {
        var cities = new LinkedList<>(r.getCityOrder());
        var ic1 = cities.indexOf(c1);
        var ic2 = cities.indexOf(c2);
        Collections.swap(cities, ic1, ic2);
        return new Route(cities);
    }
}
