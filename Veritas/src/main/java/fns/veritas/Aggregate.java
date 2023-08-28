/*
 * This file is part of Freedom-Network-Suite - https://github.com/AtlasMediaGroup/Freedom-Network-Suite
 * Copyright (C) 2023 Total Freedom Server Network and contributors
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

package fns.veritas;

import fns.patchwork.utils.logging.FNS4J;
import fns.veritas.bukkit.BukkitNative;
import fns.veritas.bukkit.ServerListener;
import fns.veritas.client.BotClient;
import fns.veritas.client.BotConfig;
import org.bukkit.Bukkit;

public class Aggregate
{
    private static final FNS4J logger = FNS4J.getLogger("Veritas");
    private final BotClient bot;
    private final Veritas plugin;
    private final BukkitNative bukkitNativeListener;
    private final ServerListener serverListener;

    public Aggregate(final Veritas plugin)
    {
        this.plugin = plugin;
        this.bot = new BotClient(new BotConfig(plugin));
        this.bukkitNativeListener = new BukkitNative(plugin);
        this.serverListener = new ServerListener(plugin);

        Bukkit.getServer().getPluginManager().registerEvents(this.getBukkitNativeListener(), plugin);
        this.getServerListener().minecraftChatBound().subscribe();
    }

    public static FNS4J getLogger()
    {
        return logger;
    }

    public ServerListener getServerListener()
    {
        return serverListener;
    }

    public BukkitNative getBukkitNativeListener()
    {
        return bukkitNativeListener;
    }

    public BotClient getBot()
    {
        return bot;
    }

    public Veritas getPlugin()
    {
        return plugin;
    }
}
