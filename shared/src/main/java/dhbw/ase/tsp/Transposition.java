package dhbw.ase.tsp;

import java.util.Collections;
import java.util.List;

public class Transposition {
    private final City c1;
    private final City c2;

    public Transposition(City c1, City c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    void applyTo(List<City> cities) {
        int ic1 = -1;
        int ic2 = -1;

        for (int i = 0 ; i < cities.size() && (ic1 == -1 || ic2 == -1) ; i++) {
            City c = cities.get(i);
            if (c == c1) {
                ic1 = i;
            }
            if (c == c2) {
                ic2 = i;
            }
        }

        Collections.swap(cities, ic1, ic2);
    }
}
