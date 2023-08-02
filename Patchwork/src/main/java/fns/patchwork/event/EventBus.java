package fns.patchwork.event;

import fns.patchwork.api.Context;
import fns.patchwork.base.Patchwork;
import fns.patchwork.service.Service;
import java.util.HashSet;
import java.util.Set;

public class EventBus extends Service
{
    private final Patchwork plugin;
    private final Set<FEvent> eventSet = new HashSet<>();
    private final SubscriptionBox<?> runningSubscriptions = new SubscriptionBox<>();

    public EventBus(final Patchwork plugin)
    {
        super("event_bus");
        this.plugin = plugin;
    }

    public void addEvent(final FEvent event)
    {
        eventSet.add(event);
    }

    public <T extends FEvent> T getEvent(final Class<T> eventClass)
    {
        final FEvent e = eventSet.stream()
                                 .filter(event -> event.getEventClass()
                                                       .equals(eventClass))
                                 .findFirst()
                                 .orElse(null);

        return eventClass.cast(e);
    }

    public <T extends FEvent> EventSubscription<T> subscribe(final Class<T> eventClass, final Callback<T> callback)
    {
        final Context<T> eventContext = () -> eventSet.stream()
                                                      .filter(event -> event.getEventClass()
                                                                            .equals(eventClass))
                                                      .findFirst()
                                                      .map(eventClass::cast)
                                                      .orElse(null);

        if (eventContext.get() == null)
        {
            throw new IllegalArgumentException("Event class " + eventClass.getName() + " is not registered.");
        }

        return new EventSubscription<>(eventContext.get(), callback);
    }

    public void unsubscribe(final EventSubscription<?> subscription)
    {
        runningSubscriptions.removeSubscription(subscription);
    }

    public Patchwork getCommonsBase()
    {
        return plugin;
    }

    @Override
    public void tick()
    {
        runningSubscriptions.tick();
    }
}
