package dhbw.ase.util.loader;

public enum Dataset {
    TEST("tsp5.txt"),
    TEST2("tsp10.txt"),
    A280("tsp280.txt");

    private final String resourceName;

    Dataset(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
