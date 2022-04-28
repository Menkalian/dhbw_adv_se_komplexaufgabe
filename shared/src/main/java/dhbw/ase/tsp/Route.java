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
        if (!(o instanceof Route route)) {
            return false;
        }
        return Objects.equals(getCityOrder(), route.getCityOrder());
    }

    @Override
    public String toString() {
        return "{" + cityOrder.stream().map(City::getName).collect(Collectors.joining(" -> ")) + "}";
    }

    public Route partialShift(int startPoint, int goalPoint, int length) {
        List<City> changed = new ArrayList<>(cityOrder);
        City[] memorized = new City[length];
        for (int i = 0 ; i < length ; i++) {
            memorized[i] = changed.get(startPoint + i);
        }

        if (startPoint < goalPoint) {
            for (int i = startPoint ; i < goalPoint ; i++) {
                changed.set(i, changed.get(i + length));
            }
        } else {
            for (int i = startPoint + length - 1 ; i >= goalPoint + length ; i--) {
                changed.set(i, changed.get(i - length));
            }
        }

        for (int i = 0 ; i < length ; i++) {
            changed.set(goalPoint + i, memorized[i]);
        }

        return new Route(changed);
    }

    public Route partialReverse(int startPoint, int length) {
        List<City> changed = new ArrayList<>(cityOrder);
        // If length is uneven the middle element does not need to be swapped
        for (int i = 0 ; i < length / 2 ; i++) {
            Collections.swap(changed, startPoint + i, startPoint + length - 1 - i);
        }

        return new Route(changed);
    }

    public Route swapped(int i1, int i2) {
        if (i1 == i2) {
            return this;
        }

        List<City> changed = new ArrayList<>(cityOrder);
        Collections.swap(changed, i1, i2);
        return new Route(changed);
    }

    public Route blockSwap(int startPoint, int goalPoint, int length) {
        List<City> changed = new ArrayList<>(cityOrder);
        for (int i = 0 ; i < length ; i++) {
            Collections.swap(changed, startPoint + i, goalPoint + 1);
        }
        return new Route(changed);
    }

    private double distanceForIndex(int i) {
        City first = cityOrder.get(i);
        City second = cityOrder.get((i + 1) % cityOrder.size());

        // We round here so we get the same result as the known optimum with the perfect route
        return Math.round(first.distance(second));
    }
}
