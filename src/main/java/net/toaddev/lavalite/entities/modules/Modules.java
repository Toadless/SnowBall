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

import net.toaddev.lavalite.modules.CacheModule;
import net.toaddev.lavalite.modules.CommandsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class Modules
{
    private static List<Module> modules = new LinkedList<>();
    private static Logger logger = LoggerFactory.getLogger(Module.class);

    /**
     *
     * @param module The {@link net.toaddev.lavalite.entities.modules.Module module} to register.
     */
    public static void registerModule(Module module)
    {
        modules.add(module);
    }

    /**
     *
     * @return Every singe {@link net.toaddev.lavalite.entities.modules.Module module} that we are using.
     */
    public static Object[] getModules()
    {
        return modules.toArray();
    }

    public static List<Module> getAllModules()
    {
        return modules;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T get(Class<T> clazz){
        var module = modules.stream().filter(mod -> mod.getClass().equals(clazz)).findFirst();
        if(module.isEmpty()){
            return null;
        }
        return (T) module.get();
    }

    /**
     *
     * @return The amount of {@link net.toaddev.lavalite.entities.modules.Module modules} that we are using.
     */
    public static int getSize()
    {
        return modules.size();
    }
}