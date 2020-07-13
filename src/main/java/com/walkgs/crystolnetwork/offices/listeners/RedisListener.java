package com.walkgs.crystolnetwork.offices.listeners;

import com.walkgs.crystolnetwork.offices.events.RedisReceiveMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class RedisListener implements Listener {

    @EventHandler
    public void onMessage(final RedisReceiveMessageEvent event) {
        final List<String> messages = (List<String>) event.getReceivedData().values();
        if (messages.size() > 0) {
            final String functionType = messages.get(0);
            if (functionType.equals("updateOffice")) {
                final UUID uuid = UUID.fromString(messages.get(2));
                final String updateType = messages.get(1);
                switch (updateType) {
                    case "forced": {

                    }
                    case "normal": {

                    }
                }
            }
        }
    }

}
