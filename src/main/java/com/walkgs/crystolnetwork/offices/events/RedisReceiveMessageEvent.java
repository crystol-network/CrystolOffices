package com.walkgs.crystolnetwork.offices.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Map;

public class RedisReceiveMessageEvent extends Event {

    private final Map<Integer, String> receivedData;

    public RedisReceiveMessageEvent(Map<Integer, String> receivedData) {
        this.receivedData = receivedData;
    }

    public RedisReceiveMessageEvent call() {
        Bukkit.getPluginManager().callEvent(this);
        return this;
    }

    public Map<Integer, String> getReceivedData() {
        return receivedData;
    }

    private static HandlerList handlerList = new HandlerList();

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
