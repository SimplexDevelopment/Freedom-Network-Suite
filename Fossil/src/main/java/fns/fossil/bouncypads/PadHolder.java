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

package fns.fossil.bouncypads;

import fns.fossil.Fossil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import fns.patchwork.base.Shortcuts;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Holds all the active pads for each player, and also manages player pad interaction.
 */
public class PadHolder implements Listener
{
    /**
     * A map of all the currently active pads, stored by {@link Player} {@link UUID}.
     */
    private final Map<UUID, BouncyPad> pads = new HashMap<>();

    /**
     * Creates a new pad holder.
     */
    public PadHolder()
    {
        Bukkit.getPluginManager()
              .registerEvents(this, Shortcuts.provideModule(Fossil.class));
    }

    /**
     * Adds a pad for the given player. If the player already has a pad stored in the map, it will be overwritten with
     * the new pad.
     *
     * @param player The player to add the pad for.
     * @param pad    The pad to add.
     */
    public void addPad(final Player player, final BouncyPad pad)
    {
        this.pads.put(player.getUniqueId(), pad);
    }

    /**
     * Removes the pad for the given player, if the player has one.
     *
     * @param player The player to remove the pad for.
     */
    public void removePad(final Player player)
    {
        this.pads.remove(player.getUniqueId());
    }

    /**
     * Gets the pad for the given player, if the player has one. If the player has no active pad, this will return
     * null.
     *
     * @param player The player to get the pad for.
     * @return The pad for the given player.
     */
    @Nullable
    public BouncyPad getPad(final Player player)
    {
        return this.pads.get(player.getUniqueId());
    }

    /**
     * Checks if there is a pad active for the given player.
     *
     * @param player The player to check.
     * @return True if the player has a pad, false otherwise.
     */
    public boolean hasPad(final Player player)
    {
        return this.pads.containsKey(player.getUniqueId());
    }

    /**
     * Gets a map of all the currently active pads, stored by {@link Player} {@link UUID}.
     *
     * @return A map of all the currently active pads.
     */
    public Map<UUID, BouncyPad> getPads()
    {
        return this.pads;
    }

    /**
     * Handles player pad interaction. This will check the relative block for each acceptible direction, and pass the
     * resulting block face (if any) to the bounce pad. See
     * {@link BouncyPad#bouncePad(Player, org.bukkit.block.BlockFace)} for how the resulting block face is processed.
     *
     * @param event The event which gets called when a player moves.
     */
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.hasPad(player))
        {
            return;
        }

        final BouncyPad pad = this.getPad(player);
        final Location location = player.getLocation();

        final Block xNeg1 = getRelative(location, -1, 0, 0);
        final Block xPos1 = getRelative(location, 1, 0, 0);
        final Block zNeg1 = getRelative(location, 0, 0, -1);
        final Block zPos1 = getRelative(location, 0, 0, 1);
        final Block yNeg1 = getRelative(location, 0, -1, 0);

        Stream.of(xNeg1, xPos1, zNeg1, zPos1, yNeg1)
              .filter(this::isWool)
              .map(block -> block.getFace(location.getBlock()))
              .findFirst()
              .ifPresent(face -> pad.bouncePad(player, face));
    }

    /**
     * Gets the relative block at the given location.
     *
     * @param location The location to get the relative block from.
     * @param x        The x mod.
     * @param y        The y mod.
     * @param z        The z mod.
     * @return The relative block.
     */
    private Block getRelative(final Location location, final int x, final int y, final int z)
    {
        return location.getBlock()
                       .getRelative(x, y, z);
    }

    /**
     * Checks if the given block is wool.
     *
     * @param block The block to check.
     * @return True if the block is wool, false otherwise.
     * @see Tag#WOOL
     */
    private boolean isWool(final Block block)
    {
        return Tag.WOOL.isTagged(block.getType());
    }
}
