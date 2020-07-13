package com.walkgs.crystolnetwork.offices.events;

import com.walkgs.crystolnetwork.offices.inject.CrystolPermissible;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.PermissibleBase;

public class PlayerInjectPermissibleEvent extends Event {

    private final Player player;

    private final CrystolPermissible newPermissible;
    private final PermissibleBase oldPermissible;

    private boolean cancelled = false;

    public PlayerInjectPermissibleEvent(Player player, CrystolPermissible newPermissible, PermissibleBase oldPermissible) {
        this.player = player;
        this.newPermissible = newPermissible;
        this.oldPermissible = oldPermissible;
    }

    public PlayerInjectPermissibleEvent call() {
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

    public CrystolPermissible getNewPermissible() {
        return newPermissible;
    }

    public PermissibleBase getOldPermissible() {
        return oldPermissible;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }

}
