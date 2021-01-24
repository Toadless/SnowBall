package uk.toadl3ss.lavalite.entities.commandmeta;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.lavalite.data.Config;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.Command;
import uk.toadl3ss.lavalite.entities.commandmeta.abs.ICommandMusic;
import uk.toadl3ss.lavalite.data.Constants;
import uk.toadl3ss.lavalite.entities.exception.CommandException;
import uk.toadl3ss.lavalite.perms.PermissionLevel;
import uk.toadl3ss.lavalite.util.DiscordUtil;

import java.util.ArrayList;

public class CommandManager
{
    // ################################################################################
    // ##                     Command Manager
    // ################################################################################
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(CommandManager.class);
    public static void executeCommand(String args[], GuildMessageReceivedEvent event, String prefix)
    {
        String command = args[0].replaceFirst("^" + prefix, "");
        Command cmd = CommandRegistry.getCommand(command);
        if (cmd == null)
        {
            return;
        }

        // Permission checks
        if (cmd.getPermissionNode().equals(PermissionLevel.BOT_ADMIN))
        {
            if (!DiscordUtil.isOwner(event.getMember().getUser())) {
                event.getChannel().sendMessage("You dont have permission to do this.").queue();
                return;
            }
        }
        if (cmd.getPermissionNode().equals(PermissionLevel.SERVER_ADMIN))
        {
            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER))
            {
                event.getChannel().sendMessage("You dont have permission to do this.").queue();
                return;
            }
        }

        // Check command type
        if (cmd.getCommandType().equals(CommandType.DEV)) {
            if (!Config.INS.getDevelopment()) {
                event.getChannel().sendMessage("You cannot run a dev command on this build.").queue();
                throw new CommandException("Dev commands are disabled.");
            }
        }

        // Music perm checks
        if (cmd instanceof ICommandMusic)
        {
            Member member = event.getGuild().getSelfMember();
            if (!member.hasPermission(Permission.VOICE_CONNECT))
            {
                event.getChannel().sendMessage("Cannot join voice channel. Error: `Missing permissions`.").queue();
                return;
            }
            if (!member.hasPermission(Permission.VOICE_SPEAK))
            {
                event.getChannel().sendMessage("Will not join channel with out permission: `VOICE_SPEAK`!").queue();
                return;
            }
        }


        String[] arguments = commandToArguments(event.getMessage().getContentRaw());
        cmd.run(arguments, event, prefix);
    }

    private static String[] commandToArguments(String cmd)
    {
        ArrayList<String> a = new ArrayList<>();
        int argi = 0;
        boolean isInQuote = false;

        for (Character ch : cmd.toCharArray()) {
            if (Character.isWhitespace(ch) && !isInQuote)
            {
                String arg = null;
                try {
                    arg = a.get(argi);
                } catch (IndexOutOfBoundsException e)
                {
                }
                if (arg != null)
                {
                    argi++;
                }

            } else if (ch.equals('"'))
            {
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

    private static ArrayList<String> writeToArg(ArrayList<String> a, int argi, char ch)
    {
        String arg = null;
        try {
            arg = a.get(argi);
        } catch (IndexOutOfBoundsException ignored)
        {
        }
        if (arg == null) {
            a.add(argi, String.valueOf(ch));
        } else {
            a.set(argi, arg + ch);
        }

        return a;
    }
}