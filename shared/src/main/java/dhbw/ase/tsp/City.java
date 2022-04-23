package dhbw.ase.tsp;

import java.util.Objects;

public class City {
    private final String name;
    private final int x;
    private final int y;

    public City(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public double distance(City other) {
        double h = this.x - other.x;
        double v = this.y - other.y;
        return Math.sqrt(h * h + v * v);
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return getName() + "(" + getX() + "," + getY() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        City city = (City) o;
        return getX() == city.getX() && getY() == city.getY() && Objects.equals(getName(), city.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getX(), getY());
    }
}
