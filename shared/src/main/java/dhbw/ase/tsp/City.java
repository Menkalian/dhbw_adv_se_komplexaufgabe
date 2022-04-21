package dhbw.ase.tsp;

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
}
