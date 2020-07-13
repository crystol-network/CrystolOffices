package com.walkgs.crystolnetwork.offices.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RedisMessageEvent extends Event {

    private final String serverName;
    private final String receivedData;

    private boolean cancelled = false;

    public RedisMessageEvent(String serverName, String receivedData) {
        this.serverName = serverName;
        this.receivedData = receivedData;
    }

    public RedisMessageEvent call() {
        Bukkit.getPluginManager().callEvent(this);
        return this;
    }

    public void setCancelled(boolean status) {
        cancelled = status;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public String getServerName() {
        return serverName;
    }

    public String getReceivedData() {
        return receivedData;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }

}
