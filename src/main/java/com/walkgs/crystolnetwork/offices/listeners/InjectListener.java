package com.walkgs.crystolnetwork.offices.listeners;

import com.walkgs.crystolnetwork.offices.api.PlayerPermission;
import com.walkgs.crystolnetwork.offices.api.base.ServerOffices;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public final class InjectListener implements Listener {

    private final ServerOffices serverOffices;
    private final PlayerPermission playerPermission;

    public InjectListener() {
        this.serverOffices = ServerOffices.getInstance();
        this.playerPermission = new PlayerPermission(serverOffices);
    }

    @EventHandler
    public final void onJoin(final PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        final PlayerPermission.UserData user = playerPermission.getUser(uuid);
        user.load();
        user.inject();

        //Test add group system
        //user.addGroup(serverOffices.getGroupLoader().getGroup("example"));
        //user.addGroup(serverOffices.getGroupLoader().getGroup("teste"));

    }

    //@EventHandler
    public final void onQuit(final PlayerQuitEvent event) {

        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        final PlayerPermission.UserData user = playerPermission.getUser(uuid);
        user.uninject();

    }

}
