package dhbw.ase.tsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        return cityOrder;
    }

    public double getTotalDistance() {
        // Iterate over the Inizes
        return IntStream.range(0,cityOrder.size())
                .mapToDouble(this::distanceForIndex)
                .sum();
    }

    private double distanceForIndex(int i) {
        City first = cityOrder.get(i);
        City second = cityOrder.get((i + 1) % cityOrder.size());

        return first.distance(second);
    }

    public Route shuffled() {
        List<City> shuffled = new ArrayList<>(cityOrder);
        Collections.shuffle(shuffled);
        return new Route(shuffled);
    }

    @Override
    public String toString() {
        return "{" + cityOrder.stream().map(City::getName).collect(Collectors.joining(" -> ")) + "}";
    }
}
