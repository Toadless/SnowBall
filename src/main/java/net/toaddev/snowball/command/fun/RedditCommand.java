package net.toaddev.snowball.command.fun;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toaddev.snowball.main.BotController;
import net.toaddev.snowball.objects.command.Command;
import net.toaddev.snowball.objects.command.CommandContext;
import net.toaddev.snowball.objects.command.options.CommandOptionString;
import net.toaddev.snowball.objects.exception.CommandException;
import net.toaddev.snowball.util.DiscordUtil;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@net.toaddev.snowball.annotation.Command
public class RedditCommand extends Command
{
    public RedditCommand()
    {
        super("reddit", "Pulls a post from reddit.");

        addOptions(
                new CommandOptionString("thread", "The reddit thread.").required()
        );
    }

    @Override
    public void run(@NotNull CommandContext ctx, @NotNull Consumer<CommandException> failure) throws ParserConfigurationException, SAXException, IOException
    {
        ctx.getEvent().acknowledge().queue();

        final MessageChannel textChannel = ctx.getChannel();

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setColor(DiscordUtil.getEmbedColor());

        final OkHttpClient okHttpClient = BotController.getModules().getHttpClient();

        Request request = new Request.Builder()
                .url("https://www.reddit.com/" + request(ctx.getOption("thread")) + "/random.json")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                textChannel.sendMessage("Error , an Okhttp error has occurred").queue();
            }

            @SuppressWarnings("SpellCheckingInspection")
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if(!response.isSuccessful())
                {
                    textChannel.sendMessage("Error please check values and retry").queue();
                    response.close();
                    return;
                }

                ResponseBody responseBody = response.body();

                if(responseBody != null) {
                    try {
                        DataArray dataArray = DataArray.fromJson(responseBody.string());

                        if(!dataArray.getObject(0).isNull("reason"))
                        {
                            String reason = dataArray.getObject(0).getString("reason");

                            if(reason.equals("private"))
                            {
                                textChannel.sendMessage("The given sub reddit is a private sub reddit which i cannot access").queue();
                                responseBody.close();
                                return;
                            }
                        }

                        DataArray firstData = dataArray.getObject(0).getObject("data").getArray("children");
                        DataObject finalDataObject = firstData.getObject(0).getObject("data");

                        boolean over_18 = finalDataObject.getBoolean("over_18");

                        var tchannel = (TextChannel) textChannel;

                        if (over_18 && !tchannel.isNSFW()) {
                            textChannel.sendMessage("Post contains nsfw content and the channel is not a NSFW Channel to show this content").queue();
                            responseBody.close();
                            return;
                        }

                        boolean isVideo = finalDataObject.getBoolean("is_video");

                        if (isVideo) {
                            textChannel.sendMessage("Post Contains video and i cannot support a video file").queue();
                            responseBody.close();
                            return;
                        }

                        boolean isMedia = finalDataObject.isNull("secure_media");

                        if (!isMedia) {
                            textChannel.sendMessage("The Post contains a video and i cannot support a video file").queue();
                            responseBody.close();
                            return;
                        }

                        boolean isGallery = !finalDataObject.isNull("is_gallery");

                        if(isGallery)
                        {
                            textChannel.sendMessage("The post contains gallery and i currently cannot send gallery data").queue();
                            responseBody.close();
                            return;
                        }

                        String title = finalDataObject.getString("title");
                        String selfText = finalDataObject.getString("selftext");
                        String url = finalDataObject.getString("url");
                        String postUrl = "https://www.reddit.com" + finalDataObject.get("permalink");
                        String post_hint;

                        if (!finalDataObject.isNull("post_hint")) {
                            post_hint = finalDataObject.getString("post_hint");
                        } else {
                            post_hint = "";
                        }

                        if(!isImage(url) && selfText.isEmpty())
                        {
                            embedBuilder.setTitle(title,postUrl)
                                    .setDescription("Post contains direct link :- " + url);

                            textChannel.sendMessage(embedBuilder.build()).queue();
                            responseBody.close();
                            return;
                        }

                        if (selfText.length() > 2000) {
                            int i = 1;
                            for (String part : getParts(selfText, 2000)) {
                                embedBuilder.setTitle(title + " || page : " + i, postUrl)
                                        .setDescription(part);
                                textChannel.sendMessage(embedBuilder.build()).queue();

                                i++;
                            }
                        } else {
                            if (!selfText.isEmpty()) {
                                embedBuilder.setTitle(title, postUrl)
                                        .setDescription(selfText);

                                textChannel.sendMessage(embedBuilder.build()).queue();
                            }
                        }

                        if (post_hint.equalsIgnoreCase("image")) {
                            embedBuilder.clear();

                            embedBuilder.setTitle(title, postUrl)
                                    .setColor(DiscordUtil.getEmbedColor())
                                    .setImage(url);

                            textChannel.sendMessage(embedBuilder.build()).queue();
                        }

                        if (embedBuilder.isEmpty()) {
                            textChannel.sendMessage("Error , retry sub reddit").queue();
                        }

                        response.close();
                        responseBody.close();
                    }
                    catch (Exception e){

                        textChannel.sendMessage("Error occurred , please try again").queue();

                        response.close();
                        responseBody.close();
                    }
                }
                else
                {
                    textChannel.sendMessage("Error").queue();
                }
                responseBody.close();
            }
        });
    }

    public String request(String subReddit)
    {
        if(subReddit.startsWith("r/"))
        {
            return subReddit;
        }
        return "r/" + subReddit;
    }

    public List<String> getParts(String string, int partitionSize)
    {
        List<String> parts = new ArrayList<>();
        int len = string.length();
        for (int i = 0; i < len; i += partitionSize) {
            parts.add(string.substring(i, Math.min(len, i + partitionSize)));
        }
        return parts;
    }

    public boolean isImage(String url)
    {
        return url.endsWith(".png") || url.endsWith(".jpg") || url.endsWith(".gif");
    }
}