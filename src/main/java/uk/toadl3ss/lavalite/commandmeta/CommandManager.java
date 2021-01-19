package uk.toadl3ss.lavalite.commandmeta;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandAdminRestricted;
import uk.toadl3ss.lavalite.commandmeta.abs.ICommandOwnerRestricted;
import uk.toadl3ss.lavalite.data.Constants;

import java.util.ArrayList;

public class CommandManager {
    // ################################################################################
    // ##                     Command Manager
    // ################################################################################
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandManager.class);
    public static void executeCommand(String args[], GuildMessageReceivedEvent event, String prefix) {
        String command = args[0].replaceFirst("^" + prefix, "");
        Command cmd = CommandRegistry.getCommand(command);
        if (cmd == null) {
            return;
        }
        if (cmd instanceof ICommandOwnerRestricted) {
            if (!event.getMember().getId().equals(Constants.ownerid)) {
                return;
            }
        }
        if (cmd instanceof ICommandAdminRestricted) {
            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                event.getChannel().sendMessage("You dont have permission to do this.").queue();
                return;
            }
        }
        String[] arguments = commandToArguments(event.getMessage().getContentRaw());
        cmd.onInvoke(arguments, event, prefix);
    }

    private static String[] commandToArguments(String cmd) {
        ArrayList<String> a = new ArrayList<>();
        int argi = 0;
        boolean isInQuote = false;

        for (Character ch : cmd.toCharArray()) {
            if (Character.isWhitespace(ch) && !isInQuote) {
                String arg = null;
                try {
                    arg = a.get(argi);
                } catch (IndexOutOfBoundsException e) {
                }
                if (arg != null) {
                    argi++;
                }

            } else if (ch.equals('"')) {
                isInQuote = !isInQuote;
            } else {
                a = writeToArg(a, argi, ch);
            }
        }

        String[] newA = new String[a.size()];
        int i = 0;
        for (String str : a) {
            newA[i] = str;
            i++;
        }

        return newA;
    }

    private static ArrayList<String> writeToArg(ArrayList<String> a, int argi, char ch) {
        String arg = null;
        try {
            arg = a.get(argi);
        } catch (IndexOutOfBoundsException ignored) {
        }
        if (arg == null) {
            a.add(argi, String.valueOf(ch));
        } else {
            a.set(argi, arg + ch);
        }

        return a;
    }
}