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

package fns.patchwork.base;

import fns.patchwork.display.adminchat.AdminChatDisplay;
import fns.patchwork.event.EventBus;
import fns.patchwork.service.FreedomExecutor;
import fns.patchwork.service.SubscriptionProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The base class for Patchwork.
 */
public class Patchwork extends JavaPlugin
{
    /**
     * The {@link EventBus} for this plugin.
     */
    private EventBus eventBus;
    /**
     * The {@link FreedomExecutor} for this plugin.
     */
    private FreedomExecutor executor;
    /**
     * The {@link AdminChatDisplay} for this plugin.
     */
    private AdminChatDisplay acdisplay;

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler()
              .runTaskLater(this, () -> Registration
                      .getServiceTaskRegistry()
                      .stopAllServices(), 1L);

        Registration.getServiceTaskRegistry()
                          .unregisterService(EventBus.class);
    }

    @Override
    public void onEnable()
    {
        eventBus = new EventBus(this);
        executor = new FreedomExecutor(this);
        acdisplay = new AdminChatDisplay(this);


        Registration.getServiceTaskRegistry()
                          .registerService(SubscriptionProvider.asyncService(this, eventBus));

        getExecutor().getSync()
                     .execute(() -> Registration
                             .getServiceTaskRegistry()
                             .startAllServices());

        Registration.getModuleRegistry().addModule(this);
    }

    /**
     * Gets the {@link FreedomExecutor} for this plugin.
     *
     * @return the {@link FreedomExecutor}
     */
    public FreedomExecutor getExecutor()
    {
        return executor;
    }

    /**
     * Gets the {@link EventBus} for this plugin. The EventBus is used to register and listen to custom events provided
     * by Freedom Network Suite.
     *
     * @return the {@link EventBus}
     */
    public EventBus getEventBus()
    {
        return eventBus;
    }

    /**
     * Gets the {@link AdminChatDisplay} for this plugin. The AdminChatDisplay is used to display messages sent in
     * adminchat.
     *
     * @return the {@link AdminChatDisplay}
     */
    public AdminChatDisplay getAdminChatDisplay()
    {
        return acdisplay;
    }
}
