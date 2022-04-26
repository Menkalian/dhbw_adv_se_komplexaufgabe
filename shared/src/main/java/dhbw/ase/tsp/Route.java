package dhbw.ase.tsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Route {
    private final List<City> cityOrder;

    public Route(List<City> cities) {
        cityOrder = Collections.unmodifiableList(cities);
    }

    public List<City> getCityOrder() {
        return cityOrder;
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
