/*
 * This file is part of FreedomNetworkSuite - https://github.com/SimplexDevelopment/FreedomNetworkSuite
 * Copyright (C) 2023 Simplex Development and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fns.patchwork.display.adminchat;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class ACFormatBuilder
{
    private char openTag = '[';
    private char closeTag = ']';
    private TextColor prefixColor = NamedTextColor.DARK_RED;
    private TextColor bracketColor = NamedTextColor.WHITE;
    private TextColor nameColor = NamedTextColor.AQUA;
    private TextColor rankColor = NamedTextColor.GOLD;
    private String prefix = "Admin";
    private String chatSplitter = ">>";

    private ACFormatBuilder()
    {

    }

    public static ACFormatBuilder format()
    {
        return new ACFormatBuilder();
    }

    public ACFormatBuilder openBracket(final char openTag)
    {
        this.openTag = openTag;
        return this;
    }

    public ACFormatBuilder closeBracket(final char closeTag)
    {
        this.closeTag = closeTag;
        return this;
    }

    public ACFormatBuilder prefixColor(final TextColor prefixColor)
    {
        this.prefixColor = prefixColor;
        return this;
    }

    public ACFormatBuilder bracketColor(final TextColor bracketColor)
    {
        this.bracketColor = bracketColor;
        return this;
    }

    public ACFormatBuilder prefix(final String prefix)
    {
        this.prefix = prefix;
        return this;
    }

    public ACFormatBuilder chatSplitter(final String chatSplitter)
    {
        this.chatSplitter = chatSplitter;
        return this;
    }

    public ACFormatBuilder nameColor(final TextColor nameColor)
    {
        this.nameColor = nameColor;
        return this;
    }

    public ACFormatBuilder rankColor(final TextColor rankColor)
    {
        this.rankColor = rankColor;
        return this;
    }

    String openBracket()
    {
        return String.valueOf(openTag);
    }

    String closeBracket()
    {
        return String.valueOf(closeTag);
    }

    TextColor prefixColor()
    {
        return prefixColor;
    }

    TextColor bracketColor()
    {
        return bracketColor;
    }

    TextColor nameColor()
    {
        return nameColor;
    }

    TextColor rankColor()
    {
        return rankColor;
    }

    String prefix()
    {
        return prefix;
    }

    String chatSplitter()
    {
        return chatSplitter;
    }

    public AdminChatFormat build()
    {
        return new AdminChatFormat(this);
    }
}
