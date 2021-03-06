package com.crystolnetwork.offices.events;

import com.crystolnetwork.offices.utils.inject.CrystolPermissible;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissibleBase;

public class PlayerUnInjectPermissibleEvent extends Event {

    private final Player player;

    private final CrystolPermissible injectedPermissible;
    private final PermissibleBase defaultPermissible;

    private boolean cancelled = false;

    public PlayerUnInjectPermissibleEvent(Player player, PermissibleBase defaultPermissible, CrystolPermissible injectedPermissible) {
        this.player = player;
        this.defaultPermissible = defaultPermissible;
        this.injectedPermissible = injectedPermissible;
    }

    public PlayerUnInjectPermissibleEvent call() {
        Bukkit.getPluginManager().callEvent(this);
        return this;
    }

    public void setCancelled(boolean status) {
        cancelled = status;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public PermissibleBase getDefaultPermissible() {
        return defaultPermissible;
    }

    public CrystolPermissible getInjectedPermissible() {
        return injectedPermissible;
    }

    private static HandlerList handlerList = new HandlerList();

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
