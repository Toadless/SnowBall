/*
 *  MIT License
 *
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of Lavalite and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of Lavalite, and to permit persons to whom Lavalite is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Lavalite.
 *
 * LAVALITE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package net.toaddev.lavalite.entities.modules;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.toaddev.lavalite.entities.exception.ModuleException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Module extends ListenerAdapter
{
    private String name;
    private final Logger logger;

    /**
     *
     * @param name The name of the {@link Module module} that we are loading.
     */
    public Module(@NotNull String name)
    {
        this.name = name;
        this.logger = LoggerFactory.getLogger(Module.class);
    }

    /**
     *
     * @return The name of the {@link Module module} that we are loading.
     */
    public String getName()
    {
        return name;
    }

    public void enable()
    {
        try
        {
            logger.info("Starting the {} module.", name);
            onEnable();
        }
        catch (Exception e)
        {
            throw new ModuleException("Error whilst starting the module: " + name + "!");
        }
    }

    public void disable()
    {
        try
        {
            logger.info("Disabled the {} module.", name);
            onDisable();
        }
        catch (Exception e)
        {
            throw new ModuleException("Error whilst disabling the module: " + name + "!");
        }
    }

    public abstract void onEnable();
    public abstract void onDisable();
}