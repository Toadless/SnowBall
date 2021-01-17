package uk.toadl3ss.lavalite.utils;

import uk.toadl3ss.lavalite.Lavalite;

public class Logger {
    // ################################################################################
    // ##                     Logger
    // ################################################################################
    // Logging an error to the console
    public static void error(String text) {
        Lavalite.logger.error(text);
    }

    // Logging info to the console
    public static void info(String text) {
        Lavalite.logger.info(text);
    }

    // Logging warns to the console
    public static void warn(String text) {
        Lavalite.logger.warn(text);
    }
}
