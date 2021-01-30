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

package net.toaddev.lavalite.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.toaddev.lavalite.main.Launcher;
import net.toaddev.lavalite.data.Constants;

public class SetActivity
{
    public static Boolean defaultStatus = false;
    public static void SetActivity(JDA jda)
    {
        if (Constants.status.equals(""))
        {
            defaultStatus = true;
        }
        if (Constants.game.equals(""))
        {
            defaultStatus = true;
        }
        if (Constants.status.equals("playing"))
        {
            if (defaultStatus)
            {
                return;
            }
            jda.getPresence().setActivity(Activity.playing(Constants.game));
            return;
        }
        if (Constants.status.equals("watching"))
        {
            if (defaultStatus)
            {
                return;
            }
            jda.getPresence().setActivity(Activity.watching(Constants.game));
            return;
        }
        if (Constants.status.equals("listening"))
        {
            if (defaultStatus)
            {
                return;
            }
            jda.getPresence().setActivity(Activity.listening(Constants.game));
            return;
        }
        if (Constants.status.equals("competing"))
        {
            if (defaultStatus)
            {
                return;
            }
            jda.getPresence().setActivity(Activity.competing(Constants.game));
            return;
        }
        if (defaultStatus)
        {
            jda.getPresence().setActivity(Activity.playing("Lavalite v" + Launcher.version));
        }
    }
}