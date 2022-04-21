package dhbw.ase.util.loader;

public enum Dataset {
    A280("tsp280.txt");

    private final String resourceName;

    Dataset(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
