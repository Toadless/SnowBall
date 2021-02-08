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

package net.toaddev.snowball.entities;

public enum Emoji
{
    INBOX_TRAY("\uD83D\uDCE5"),
    OUTBOX_TRAY("\uD83D\uDCE4"),
    ROBOT("\uD83E\uDD16"),

    ARROW_LEFT("\u2B05\uFE0F"),
    ARROW_RIGHT("\u27A1\uFE0F"),
    BACK("\u25C0"),
    PLAY_PAUSE("\u23ef\ufe0f"),
    FORWARD("\u25B6"),
    WASTEBASKET("\uD83D\uDDD1\uFE0F"),
    SHUFFLE("\uD83D\uDD00"),
    VOLUME_DOWN("\uD83D\uDD09"),
    VOLUME_UP("\uD83D\uDD0A"),
    X("\u274C"),
    QUESTION("\u2753"),
    CHECK("\u2705"),
    CAT("\uD83D\uDC31"),
    DOG("\uD83D\uDC36");

    private final long emoteId;
    private final boolean isAnimated;
    private final String unicode;

    Emoji(long emoteId)
    {
        this.emoteId = emoteId;
        this.isAnimated = false;
        this.unicode = null;
    }

    Emoji(String unicode)
    {
        this.emoteId = 0;
        this.isAnimated = false;
        this.unicode = unicode;
    }

    public long getId()
    {
        return this.emoteId;
    }

    public String get()
    {
        if (this.unicode == null)
        {
            if (this.isAnimated)
            {
                return "<a:emote:" + this.emoteId + ">";
            }
            return "<:emote:" + this.emoteId + ">";
        }
        return this.unicode;
    }
}