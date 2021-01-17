package uk.toadl3ss.lavalite.commandmeta;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;

public class CommandManager {
    // ################################################################################
    // ##                     Command Manager
    // ################################################################################
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandManager.class);
    public static void executeCommand(String args[], MessageReceivedEvent event, String prefix) {
        String command = args[0].replaceFirst("^" + prefix, "");
        Command cmd = CommandRegistry.getCommand(command);
        if (cmd == null) {
            return;
        }
        cmd.onInvoke(args, event, prefix, command);
    }
}