package dhbw.ase.tsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Route {
    private final List<City> cityOrder = new ArrayList<>();

    public Route(List<City> cities) {
        cityOrder.addAll(cities);
    }

    public Route(Route route) {
        cityOrder.addAll(route.cityOrder);
    }

    public List<City> getCityOrder() {
        return Collections.unmodifiableList(cityOrder);
    }

    public double getTotalDistance() {
        // Iterate over the Indizes
        return IntStream.range(0, cityOrder.size())
                        .mapToDouble(this::distanceForIndex)
                        .sum();
    }

    public Route shuffled() {
        List<City> shuffled = new ArrayList<>(cityOrder);
        Collections.shuffle(shuffled);
        return new Route(shuffled);
    }

    public Transpositions difference(Route other) {
        //Difference is defined as the set of swaps necessary to reach the same route
        List<City> adaptedRoute = new LinkedList<>(cityOrder);
        List<City> otherRoute = other.getCityOrder();

        List<Transposition> transpositions = new LinkedList<>();

        for (int i = 0 ; i < other.cityOrder.size() ; i++) {
            City c1 = adaptedRoute.get(i);
            City c2 = otherRoute.get(i);

            if (!c1.equals(c2)) {
                transpositions.add(new Transposition(c1, c2));
                // Apply swap to the adapted route
                Collections.swap(adaptedRoute, i, adaptedRoute.indexOf(c2));
            }
        }

        // TODO: Reduce to minimal amount of swaps

        return new Transpositions(transpositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCityOrder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Route)) {
            return false;
        }
        Route route = (Route) o;
        return Objects.equals(getCityOrder(), route.getCityOrder());
    }

    @Override
    public String toString() {
        return "{" + cityOrder.stream().map(City::getName).collect(Collectors.joining(" -> ")) + "}";
    }

    private double distanceForIndex(int i) {
        City first = cityOrder.get(i);
        City second = cityOrder.get((i + 1) % cityOrder.size());

        return first.distance(second);
    }
}
