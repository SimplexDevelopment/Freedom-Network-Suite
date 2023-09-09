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

package fns.patchwork.base;

import fns.patchwork.provider.ExecutorProvider;
import fns.patchwork.sql.SQL;
import fns.patchwork.user.User;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Shortcuts
{
    private Shortcuts()
    {
        throw new AssertionError();
    }

    public static <T extends JavaPlugin> T provideModule(final Class<T> pluginClass)
    {
        return Registration.getModuleRegistry()
                           .getProvider(pluginClass)
                           .getModule();
    }

    public static User getUser(final Player player)
    {
        return Registration.getUserRegistry()
                           .getUser(player);
    }

    public static ExecutorProvider getExecutors()
    {
        return provideModule(Patchwork.class).getExecutor();
    }

    public static Optional<SQL> getSQL()
    {
        return Registration.getSQLRegistry().getSQL();
    }
}
