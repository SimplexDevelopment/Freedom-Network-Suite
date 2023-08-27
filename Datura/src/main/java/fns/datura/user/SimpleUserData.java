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

package fns.datura.user;

import fns.datura.event.UserDataUpdateEvent;
import fns.datura.perms.FreedomUser;
import fns.patchwork.base.Patchwork;
import fns.patchwork.base.Registration;
import fns.patchwork.base.Shortcuts;
import fns.patchwork.display.adminchat.AdminChatFormat;
import fns.patchwork.permissible.Group;
import fns.patchwork.sql.SQL;
import fns.patchwork.user.User;
import fns.patchwork.user.UserData;
import fns.patchwork.utils.logging.FNS4J;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleUserData implements UserData
{
    private final UUID uuid;
    private final String username;
    private final User user;
    private final UserDataUpdateEvent event = new UserDataUpdateEvent(this);
    private Group group;
    private long playtime;
    private boolean canInteract;
    private AtomicLong balance;
    private boolean transactionsFrozen;
    private boolean hasCustomACFormat = false;
    private String customACFormat;

    public SimpleUserData(final Player player)
    {
        this.uuid = player.getUniqueId();
        this.username = player.getName();
        this.user = new FreedomUser(player);

        Shortcuts.provideModule(Patchwork.class)
                 .getEventBus()
                 .addEvent(event);
    }

    private SimpleUserData(
            final UUID uuid,
            final String username,
            final User user,
            final Group group,
            final long playtime,
            final boolean canInteract,
            final long balance,
            final boolean transactionsFrozen)
    {
        this.uuid = uuid;
        this.username = username;
        this.user = user;
        this.group = group;
        this.playtime = playtime;
        this.canInteract = canInteract;
        this.balance = new AtomicLong(balance);
        this.transactionsFrozen = transactionsFrozen;
        this.customACFormat = AdminChatFormat.DEFAULT.serialize();
    }

    public static SimpleUserData fromSQL(final SQL sql, final String uuid)
    {
        return sql.executeQuery("SELECT * FROM users WHERE UUID = ?", uuid)
                  .thenApplyAsync(result ->
                  {
                      try
                      {
                          if (result.next())
                          {
                              final String g = result.getString("group");

                              final UUID u = UUID.fromString(uuid);
                              final String username = result.getString("username");

                              final Player player = Bukkit.getPlayer(u);

                              if (player == null)
                                  throw new IllegalStateException("Player should be online but they are not!");

                              final User user = new FreedomUser(player);
                              final Group group = Registration
                                                           .getGroupRegistry()
                                                           .getGroup(g);

                              final long playtime = result.getLong("playtime");
                              final boolean canInteract = result.getBoolean("canInteract");
                              final long balance = result.getLong("balance");
                              final boolean transactionsFrozen = result.getBoolean("transactionsFrozen");

                              return new SimpleUserData(u, username, user, group, playtime,
                                      canInteract, balance, transactionsFrozen);
                          }
                      } catch (SQLException ex)
                      {
                          final String sb = "An error occurred while trying to retrieve user data for" +
                                  " UUID " +
                                  uuid +
                                  " from the database." +
                                  "\nCaused by: " +
                                  ExceptionUtils.getRootCauseMessage(ex) +
                                  "\nStack trace: " +
                                  ExceptionUtils.getStackTrace(ex);

                          FNS4J.getLogger("Datura")
                               .error(sb);
                      }

                      final Player player = Bukkit.getPlayer(UUID.fromString(uuid));
                      if (player == null) throw new IllegalStateException("Player should be online but they are not!");

                      return new SimpleUserData(player);
                  }, Shortcuts.provideModule(Patchwork.class)
                              .getExecutor()
                              .getAsync())
                  .join();
    }

    @Override
    public @NotNull UUID getUniqueId()
    {
        return uuid;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public User getUser()
    {
        return user;
    }

    @Override
    public @Nullable Group getGroup()
    {
        return group;
    }

    @Override
    public void setGroup(@Nullable final Group group)
    {
        event.ping();
        this.group = group;
    }

    @Override
    public long getPlaytime()
    {
        return playtime;
    }

    @Override
    public void setPlaytime(final long playtime)
    {
        event.ping();
        this.playtime = playtime;
    }

    @Override
    public void addPlaytime(final long playtime)
    {
        event.ping();
        this.playtime += playtime;
    }

    @Override
    public void resetPlaytime()
    {
        event.ping();
        this.playtime = 0L;
    }

    @Override
    public boolean canInteract()
    {
        return canInteract;
    }

    @Override
    public void setInteractionState(final boolean canInteract)
    {
        event.ping();
        this.canInteract = canInteract;
    }

    @Override
    public boolean areTransactionsFrozen()
    {
        return transactionsFrozen;
    }

    @Override
    public long getBalance()
    {
        return balance.get();
    }

    @Override
    public void setBalance(final long newBalance)
    {
        balance.set(newBalance);
    }

    @Override
    public long addToBalance(final long amount)
    {
        return balance.addAndGet(amount);
    }

    @Override
    public long removeFromBalance(final long amount)
    {
        return balance.addAndGet(-amount);
    }

    @Override
    public boolean hasCustomACFormat()
    {
        return hasCustomACFormat;
    }

    @Override
    public AdminChatFormat getCustomACFormat()
    {
        return AdminChatFormat.deserialize(customACFormat);
    }

    @Override
    public void setCustomACFormat(final String format)
    {
        this.hasCustomACFormat = format.equals(AdminChatFormat.DEFAULT.serialize());
        this.customACFormat = format;
    }
}
