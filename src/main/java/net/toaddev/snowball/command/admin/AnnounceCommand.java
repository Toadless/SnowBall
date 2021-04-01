package net.toaddev.snowball.command.admin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.command.CommandFlag;
import net.toaddev.snowball.objects.command.options.CommandOptionString;
import net.toaddev.snowball.objects.exception.CommandException;
import net.toaddev.snowball.util.DiscordUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class AnnounceCommand extends Command
{
    public AnnounceCommand()
    {
        super("announce", "Lets bot admins announce to the bots servers.");

        addFlags(CommandFlag.DEVELOPER_ONLY);

        addOptions(
                new CommandOptionString("message", "The text that you are going to announce.").required()
        );
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure)
    {
        announce(new EmbedBuilder()
                        .setTitle("Announcement")
                        .setColor(DiscordUtil.getEmbedColor())
                        .setDescription(ctx.getOption("message"))
                        .build());
        ctx.getEvent().reply("Announced the message!").queue();
    }

    public void announce(MessageEmbed embed)
    {
        BotController.jda.getGuilds().forEach(guild -> {
            try
            {
                if (guild.getSystemChannel() != null)
                {
                    guild.getSystemChannel().sendMessage(embed).queue();
                    return;
                }
                else if (guild.getDefaultChannel() != null)
                {
                    guild.getDefaultChannel().sendMessage(embed).queue();
                }
            }
            catch (Exception ignored)
            {

            }
        });
    }
}
