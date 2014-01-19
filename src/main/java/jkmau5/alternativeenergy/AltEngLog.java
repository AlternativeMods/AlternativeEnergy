package jkmau5.alternativeenergy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * No description given
 *
 * @author jk-5
 */
@SuppressWarnings("unused")
public class AltEngLog {

    private static final Logger logger = LogManager.getLogger("AlternativeEnergy");

    public static void log(Level level, String format, Object... data) {
        logger.log(level, String.format(format, data));
    }

    public static void log(Level level, Throwable ex, String format, Object... data) {
        logger.log(level, String.format(format, data), ex);
    }

    public static void fatal(Throwable ex, String format, Object... data) {
        log(Level.FATAL, ex, format, data);
    }

    public static void fatal(String format, Object... data) {
        log(Level.FATAL, format, data);
    }

    public static void error(Throwable ex, String format, Object... data) {
        log(Level.ERROR, ex, format, data);
    }

    public static void error(String format, Object... data) {
        log(Level.ERROR, format, data);
    }

    public static void warning(Throwable ex, String format, Object... data) {
        log(Level.WARN, ex, format, data);
    }

    public static void warning(String format, Object... data) {
        log(Level.WARN, format, data);
    }

    public static void info(String format, Object... data) {
        log(Level.INFO, format, data);
    }

    public static void fine(String format, Object... data) {
        log(Level.TRACE, format, data);
    }

    public static void finer(String format, Object... data) {
        log(Level.DEBUG, format, data);
    }

    public static Logger getLogger() {
        return logger;
    }
}
