package dhbw.ase.app2;

public enum Config {
    INSTANCE;

    public final int parallelThreads = Runtime.getRuntime().availableProcessors();

}
