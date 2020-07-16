package com.walkgs.crystolnetwork.offices.listeners;

import com.walkgs.crystolnetwork.offices.api.PlayerBase;
import com.walkgs.crystolnetwork.offices.api.PlayerPermission;
import com.walkgs.crystolnetwork.offices.api.services.OfficesServices;
import com.walkgs.crystolnetwork.offices.services.classlife.Singleton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public final class InjectListener implements Listener {

    private final OfficesServices officesServices = Singleton.getOrFill(OfficesServices.class);
    private final PlayerBase playerBase = officesServices.getPlayerBase();

    @EventHandler
    public final void onJoin(final PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        final PlayerPermission user = playerBase.getUser(uuid);
        user.load();
        user.inject();

        //Test add group system
        user.addGroup(officesServices.getGroupLoader().getGroup("example"));
        user.addGroup(officesServices.getGroupLoader().getGroup("teste"));

    }

    //@EventHandler
    public final void onQuit(final PlayerQuitEvent event) {

        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        final PlayerPermission user = playerBase.getUser(uuid);
        user.uninject();

    }

}
