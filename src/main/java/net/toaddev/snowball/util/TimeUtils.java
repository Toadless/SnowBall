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

package net.toaddev.snowball.util;

import java.time.Duration;

public class TimeUtils
{
    private TimeUtils()
    {
        // override, default, constructor
    }



    public static String fTime(int time){
        return time > 9 ? String.valueOf(time) : "0" + time;
    }

    public static String formatDuration(long length){
        var duration = Duration.ofMillis(length);
        var hours = duration.toHours();
        if(hours > 0){
            return String.format("%s:%s:%s", fTime((int) hours), fTime(duration.toMinutesPart()), fTime(duration.toSecondsPart()));
        }
        return String.format("%s:%s", fTime((int) duration.toMinutes()), fTime(duration.toSecondsPart()));
    }
}