package dhbw.ase.app3.search.pso;

public enum Config {
    INSTANCE;

    public final int parallelThreads = Runtime.getRuntime().availableProcessors();

}