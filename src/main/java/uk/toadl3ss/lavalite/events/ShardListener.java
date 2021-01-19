package uk.toadl3ss.lavalite.events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class ShardListener implements EventListener {
    private GenericEvent lastEvent = null;
    private long lastEventTime = System.currentTimeMillis();
    private long eventCount = 0;

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        lastEvent = event;
        lastEventTime = System.currentTimeMillis();
        eventCount++;
    }

    public GenericEvent getLastEvent() {
        return lastEvent;
    }

    public long getLastEventTime() {
        return lastEventTime;
    }

    public long getEventCount() {
        return eventCount;
    }
}