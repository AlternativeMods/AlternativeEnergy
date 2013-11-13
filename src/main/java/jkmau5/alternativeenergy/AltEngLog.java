package jkmau5.alternativeenergy;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main logger for AlternativeEnergy. Don't use System.out, because it's ugly, use this!
 *
 * @author jk-5
 */
public class AltEngLog {

    private static final Logger altEngLogger = Logger.getLogger("AlternativeEnergy");

    static {
        altEngLogger.setParent(FMLRelaunchLog.log.getLogger());
    }

    public static void log(Level level, String format, Object... data) {
        altEngLogger.log(level, String.format(format, data));
    }

    public static void log(Level level, Throwable ex, String format, Object... data) {
        altEngLogger.log(level, String.format(format, data), ex);
    }

    public static void severe(Throwable ex, String format, Object... data) {
        log(Level.SEVERE, ex, format, data);
    }

    public static void severe(String format, Object... data) {
        log(Level.SEVERE, format, data);
    }

    public static void warning(String format, Object... data) {
        log(Level.WARNING, format, data);
    }

    public static void warning(Throwable ex, String format, Object... data) {
        log(Level.WARNING, ex, format, data);
    }

    public static void info(String format, Object... data) {
        log(Level.INFO, format, data);
    }

    public static void fine(String format, Object... data) {
        log(Level.FINE, format, data);
    }

    public static void finer(String format, Object... data) {
        log(Level.FINER, format, data);
    }

    public static void finest(String format, Object... data) {
        log(Level.FINEST, format, data);
    }

    public static Logger getLogger() {
        return altEngLogger;
    }
}
