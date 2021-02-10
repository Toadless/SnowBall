package net.toaddev.snowball.modules;

import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toaddev.snowball.api.API;
import net.toaddev.snowball.data.Config;
import net.toaddev.snowball.entities.module.Module;
import net.toaddev.snowball.main.Launcher;
import okhttp3.*;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RequestModule extends Module
{
    private static final Logger LOG = LoggerFactory.getLogger(RequestModule.class);

    private final Request.Builder requestBuilder = new Request.Builder().header("user-agent", "de.kittybot");

    public void executeAsync(Request request, BiConsumer<Call, Response> success, BiConsumer<Call, Response> error)
    {
        executeAsync(request, null, success, error);
    }

    public void executeAsync(Request request, API api, BiConsumer<Call, Response> success, BiConsumer<Call, Response> error)
    {
        this.modules.getHttpClient().newCall(request).enqueue(new Callback()
        {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                LOG.error("There was an error while sending a request to {}", call.request().url(), e);
                if(error != null)
                {
                    error.accept(call, null);

                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
            {
                var code = response.code();
                if(code != 200)
                {
                    var stringBody = "null";
                    try(var body = response.body())
                    {
                        if(body != null)
                        {
                            stringBody = body.string();
                        }
                    }
                    catch(IOException ignored){
                    }
                    LOG.warn("Failed to send a request to {} | code: {} | response: {}", call.request().url(), code, stringBody);
                    if(error != null)
                    {
                        error.accept(call, response);
                    }
                    response.close();
                    return;
                }
                if(api != null)
                {
                    LOG.info("Successfully executed a stats update request to {} API", api.getName());
                }
                if(success != null)
                {
                    success.accept(call, response);
                }
                response.close();
            }

        });
    }

    public String executeRequest(Request request, API api)
    {
        var requestUrl = request.url();
        try(var response = this.modules.getHttpClient().newCall(request).execute())
        {
            var body = response.body();
            var code = response.code();
            if(code != 200 || body == null)
            {
                var string = body == null ? null : body.string();
                LOG.warn("Failed to send a request to {} | code: {} | response: {}", requestUrl, code, string);
                return string;
            }
            if(api != null)
            {
                LOG.info("Successfully executed a stats update request to {} API", api.getName());
            }
            return body.string();
        }
        catch(Exception e)
        {
            LOG.error("There was an error while sending a request to {}", requestUrl, e);
        }
        return "";
    }

    public void updateStats(API api, int guildCount, String token)
    {
        var requestBody = RequestBody.create(
                DataObject.empty()
                        .put(api.getStatsParameter(), guildCount)
                        .toString(),
                MediaType.parse("application/json; charset=utf-8")
        );
        this.requestBuilder.url(String.format(api.getUrl(), Launcher.getJda().getSelfUser().getIdLong()));
        this.requestBuilder.header("Authorization", token);
        this.requestBuilder.post(requestBody);
        executeAsync(requestBuilder.build(), api);
    }

    public void executeAsync(Request request, API api)
    {
        executeAsync(request, api, null, null);
    }
}
