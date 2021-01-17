package uk.toadl3ss.lavalite.utils;

public class Vanity {
    public static String getVanity() {
        // ################################################################################
        // ##                     Printing Vanity
        // ################################################################################
        String red = "[31m";
        String green = "[32m";
        String defaultC = "[0m";

        String vanity = ("    g   .  r _                  _ _ _       g__ _ _\n" +
                "    g  /\\\\ r| | __ ___   ____ _| (_) |_ ___ g\\ \\ \\ \\\n" +
                "    g ( ( )r| |/ _` \\ \\ / / _` | | | __/ _ \\g \\ \\ \\ \\\n" +
                "    g  \\\\/ r| | (_| |\\ V / (_| | | | ||  __/g  ) ) ) )\n" +
                "    g   '  r|_|\\__,_| \\_/ \\__,_|_|_|\\__\\___|g / / / /\n" +
                "    d======================================g/_/_/_/d");

        vanity = vanity.replace("r", red);
        vanity = vanity.replace("g", green);
        vanity = vanity.replace("d", defaultC);
        return vanity;
    }
}