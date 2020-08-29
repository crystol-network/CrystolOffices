package com.crystolnetwork.offices.listeners;

import com.crystolnetwork.offices.entity.PlayerBase;
import com.crystolnetwork.offices.entity.PlayerPermission;
import com.crystolnetwork.offices.services.OfficesServices;
import com.crystolnetwork.offices.services.SingletonService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public final class InjectListener implements Listener {

    private final OfficesServices officesServices = SingletonService.getOrFill(OfficesServices.class);
    private final PlayerBase playerBase = officesServices.getPlayerBase();

    @EventHandler
    public final void onJoin(final PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        final PlayerPermission user = playerBase.getUser(uuid);
        user.load();
        user.inject();

    }

}
