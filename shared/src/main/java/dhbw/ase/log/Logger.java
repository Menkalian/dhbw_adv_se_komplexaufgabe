package dhbw.ase.log;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final int MAX_LOG_NAME_LENGTH = 20;
    private static final Logger log = new Logger("Logging");

    private static LogLevel maxLogLevel = LogLevel.INFO;
    private final String name;

    private Logger(String name) {
        if (name.length() > MAX_LOG_NAME_LENGTH) {
            this.name = name.substring(0, MAX_LOG_NAME_LENGTH);
        } else {
            this.name = name;
        }
    }

    public static void setLogLevel(LogLevel level) {
        maxLogLevel = level;
        log.debug("Setze LogLevel auf %s", maxLogLevel);
    }

    public static Logger getLogger(String name) {
        return new Logger(name != null ? name : "null");
    }

    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz != null ? clazz.getName() : "null");
    }

    public static Logger getLogger(Object thiz) {
        return new Logger(thiz != null ? thiz.toString() : "null");
    }

    private static synchronized void logEvent(LogEvent event) {
        String formattedMsg = String.format(event.formatString, event.formatArgs);
        String logMsg = String.format("%s%s [%" + MAX_LOG_NAME_LENGTH + "s] <%6s> %s%s",
                                      getColorCode(event.level),
                                      formatTimestamp(event.timestamp),
                                      event.loggerName,
                                      event.level,
                                      formattedMsg,
                                      getColorFinish());
        System.out.println(logMsg);
    }

    private static String formatTimestamp(long timestampMillis) {
        StringBuilder toReturn = new StringBuilder();
        Instant instant = Instant.ofEpochMilli(timestampMillis);
        String time = OffsetDateTime
                .ofInstant(instant, ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        toReturn.append(time);

        while (toReturn.length() < 23) {
            toReturn.append(" ");
        }

        return toReturn.toString();
    }

    private static String getColorCode(LogLevel lvl) {
        return switch (lvl) {
            case TRACE -> "\u001B[38;2;100;100;100m";
            case DEBUG -> "\u001B[38;2;110;70;110m";
            case INFO -> "\u001B[38;2;10;110;210m";
            case WARN -> "\u001B[38;2;210;140;10m";
            case ERROR -> "\u001B[38;2;200;20;0m";
            case SYSTEM -> "\u001B[38;2;247;0;255m";
        };
    }

    private static String getColorFinish() {
        return "\u001B[39m";
    }

    public void trace(String msg, Object... args) {
        log(LogLevel.TRACE, msg, args);
    }

    public void debug(String msg, Object... args) {
        log(LogLevel.DEBUG, msg, args);
    }

    public void info(String msg, Object... args) {
        log(LogLevel.INFO, msg, args);
    }

    public void warn(String msg, Object... args) {
        log(LogLevel.WARN, msg, args);
    }

    public void error(String msg, Object... args) {
        log(LogLevel.ERROR, msg, args);
    }

    public void system(String msg, Object... args) {
        log(LogLevel.SYSTEM, msg, args);
    }

    public void log(LogLevel level, String msg, Object... args) {
        // Discard unnecessary logs
        if (level.getVisibility() < maxLogLevel.getVisibility()) {
            return;
        }

        long ts = System.currentTimeMillis();

        LogEvent event = new LogEvent(name, ts, level, msg, args);
        logEvent(event);
    }

    class LogEvent {
        final String loggerName;
        final long timestamp;
        final LogLevel level;
        final String formatString;
        final Object[] formatArgs;

        public LogEvent(String loggerName, long timestamp, LogLevel level, String formatString, Object... formatArgs) {
            this.loggerName = loggerName;
            this.timestamp = timestamp;
            this.level = level;
            this.formatString = formatString;
            this.formatArgs = formatArgs;
        }
    }
}
