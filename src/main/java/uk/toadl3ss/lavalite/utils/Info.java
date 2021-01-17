package uk.toadl3ss.lavalite.utils;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import uk.toadl3ss.lavalite.Lavalite;
import uk.toadl3ss.lavalite.data.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Info {
    public static String getInfo() {
        // ################################################################################
        // ##                     Logging Info
        // ################################################################################
        String indentation = "\t\t\t";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");
        String startTime = format.format(new Date());
        int Cores = Runtime.getRuntime().availableProcessors();
        String info = new StringBuilder()
                .append(indentation + "Version:        ") .append(Lavalite.version + "\n")
                .append(indentation + "Development:    ") .append(Config.INS.getDevelopment() + "\n")
                .append(indentation + "Cores:          ") .append(Cores + "\n")
                .append(indentation + "Author:         ") .append("Toadless" + "\n")
                .append(indentation + "StartTime:      ") .append(startTime + "\n")
                .append(indentation + "JVM:            ") .append(System.getProperty("java.version") + "\n")
                .append(indentation + "Lavaplayer      ") .append(PlayerLibrary.VERSION + "\n")
                .toString()
        ;
        return info;
    }
}