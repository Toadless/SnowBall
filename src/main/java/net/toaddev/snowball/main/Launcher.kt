package net.toaddev.snowball.main

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary
import net.dv8tion.jda.api.JDAInfo
import net.toaddev.snowball.entities.BotControllerBuilder
import net.toaddev.snowball.util.IOUtil
import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.text.SimpleDateFormat
import java.util.*

@SpringBootApplication
open class SnowballApplication

object Launcher {
    private val log = LoggerFactory.getLogger(Launcher::class.java)
    private var version = "Unknown"

    val startTime = System.currentTimeMillis()

    private fun getVersionInfo(indentation: String = "\t", vanity: Boolean = true): String {
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm:ss z")
        val startTime = format.format(Date())
        val cores = Runtime.getRuntime().availableProcessors()

        return buildString {
            if (vanity) {
                appendLine()
                appendLine()
                appendLine(getVanity())
            }
            appendLine()
            append("${indentation}Version:        "); appendLine(version)
            append("${indentation}Author:         "); appendLine("Toadless")
            append("${indentation}Cores:          "); appendLine(cores)
            append("${indentation}StartTime:      "); appendLine(startTime)
            append("${indentation}JVM:            "); appendLine(System.getProperty("java.version"))
            append("${indentation}Lavaplayer:     "); appendLine(PlayerLibrary.VERSION)
            append("${indentation}JDA:            "); appendLine(JDAInfo.VERSION)

            appendLine()
            appendLine()
        }
    }

    private fun getVanity(): String {
        val red = "[31m"
        val green = "[32m"
        val defaultC = "[0m"
        var vanity = ("g       .r                           _           _ _  g__ _ _\n" +
                "g      /\\\\  r___ _ __   _____      _| |__   __ _| | | g\\ \\ \\ \\\n" +
                "g     ( ( )/ r__| '_ \\ / _ \\ \\ /\\ / / '_ \\ / _` | | |  g\\ \\ \\ \\\n" +
                "g      \\\\/ r\\__ \\ | | | (_) \\ V  V /| |_) | (_| | | |   g) ) ) )\n" +
                "g       '  r|___/_| |_|\\___/ \\_/\\_/ |_.__/ \\__,_|_|_|  g/ / / /\n" +
                "g    d================================================g/_/_/_/d")
        vanity = vanity.replace("r", red)
        vanity = vanity.replace("g", green)
        vanity = vanity.replace("d", defaultC)
        return vanity
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val javaVersionMajor = Runtime.version().feature()
        if (javaVersionMajor != 15) {
            BotController.logger.warn("""
		 __      ___   ___ _  _ ___ _  _  ___ 
		 \ \    / /_\ | _ \ \| |_ _| \| |/ __|
		  \ \/\/ / _ \|   / .` || || .` | (_ |
		   \_/\_/_/ \_\_|_\_|\_|___|_|\_|\___|
		                                      """)
            BotController.logger.warn("Snowball only officially supports Java 15. You are running Java {}", Runtime.version())
        }

        if (args.isNotEmpty() &&
                (args[0].equals("-v", ignoreCase = true) || args[0].equals("--version", ignoreCase = true))) {
            println(getVersionInfo(indentation = "", vanity = false))
            return
        }

        val xml = IOUtil.getResourceFileContents("snowball/snowball.xml") // Loading xml
        val xmlDoc = IOUtil.convertStringToXMLDocument(xml)

        version = IOUtil.getXmlVal(xmlDoc, "Version")

        log.info(getVersionInfo())

        val sa = SpringApplication(SnowballApplication::class.java)
        sa.setBannerMode(Banner.Mode.OFF) // We have our own
        sa.run(*args)

        BotControllerBuilder()
                .setXmlDoc(xmlDoc)
                .build() // Starting the bot

        log.info("You can safely ignore the big red warning about illegal reflection.")
    }
}