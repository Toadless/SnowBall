/*
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.toaddev.snowball.services;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.toaddev.snowball.entities.exception.ModuleNotFoundException;
import net.toaddev.snowball.entities.module.Module;
import net.toaddev.snowball.main.Launcher;
import net.toaddev.snowball.util.ThreadFactoryHelper;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *  This class loads all of the bots modules
 */
public class Modules
{
    private static final String MODULE_PACKAGE = "net.toaddev.snowball.modules";
    private static final Logger LOG = LoggerFactory.getLogger(Modules.class);

    private final ScheduledExecutorService scheduler;
    private final OkHttpClient httpClient;
    public final List<Module> modules;

    public Modules(){
        this.httpClient = new OkHttpClient();
        this.modules = new LinkedList<>();
        this.scheduler = new ScheduledThreadPoolExecutor(2, new ThreadFactoryHelper());
        loadModules();
    }

    private void loadModules()
    {
        LOG.info("Loading modules...");
        try(var result = new ClassGraph().acceptPackages(MODULE_PACKAGE).scan())
        {
            var queue = result.getSubclasses(Module.class.getName()).stream()
                    .map(ClassInfo::loadClass)
                    .filter(Module.class::isAssignableFrom)
                    .map(clazz ->
                    {
                        try
                        {
                            return ((Module) clazz.getDeclaredConstructor().newInstance()).init(this);
                        }
                        catch(Exception e)
                        {
                            LOG.info("Error whilst attempting to load modules.", e);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedList::new));

            while(!queue.isEmpty())
            {
                var instance = queue.remove();
                var dependencies = instance.getDependencies();
                if(dependencies != null && !dependencies.stream().allMatch(mod -> this.modules.stream().anyMatch(module -> mod == module.getClass())))
                {
                    queue.add(instance);
                    LOG.info("Added '{}' back to the queue. Dependencies: {}", instance.getClass().getSimpleName(), dependencies.toString());
                    continue;
                }
                instance.onEnable();
                this.modules.add(instance);
            }
        }
        LOG.info("Finished loading {} modules", this.modules.size());
    }

    public Object[] getModules()
    {
        return this.modules.toArray();
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> clazz)
    {
        var module = this.modules.stream().filter(mod -> mod.getClass().equals(clazz)).findFirst();
        if(module.isEmpty()){
            throw new ModuleNotFoundException(clazz);
        }
        return (T) module.get();
    }

    public JDA getJDA()
    {
        return Launcher.getJda();
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initDelay, long delay, TimeUnit timeUnit){
        return this.scheduler.scheduleAtFixedRate(() -> {
            try{
                runnable.run();
            }
            catch(Exception e){
                LOG.error("Unexpected error in scheduler", e);
            }
        }, initDelay, delay, timeUnit);
    }

    public ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit timeUnit){
        return this.scheduler.schedule(() -> {
            try{
                runnable.run();
            }
            catch(Exception e){
                LOG.error("Unexpected error in scheduler", e);
            }
        }, delay, timeUnit);
    }

    public Guild getGuildById(long guildId)
    {
        return getJDA().getGuildById(guildId);
    }

    public OkHttpClient getHttpClient(){
        return this.httpClient;
    }
}