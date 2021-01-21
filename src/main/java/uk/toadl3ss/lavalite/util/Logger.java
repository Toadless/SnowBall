package uk.toadl3ss.lavalite.util;

import uk.toadl3ss.lavalite.main.Launcher;

public class Logger {
    // ################################################################################
    // ##                     Logger
    // ################################################################################
    // Logging an error to the console
    public static void error(String text) {
        Launcher.logger.error(text);
    }

    // Logging info to the console
    public static void info(String text) {
        Launcher.logger.info(text);
    }

    // Logging warns to the console
    public static void warn(String text) {
        Launcher.logger.warn(text);
    }
}
