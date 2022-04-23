package dhbw.ase.log;

public enum LogLevel implements Comparable<LogLevel> {
    TRACE(100),
    DEBUG(200),
    INFO(300),
    WARN(400),
    ERROR(500),
    SYSTEM(600);
    private final int visibility;

    LogLevel(int visibility) {
        this.visibility = visibility;
    }

    public int getVisibility() {
        return visibility;
    }
}
