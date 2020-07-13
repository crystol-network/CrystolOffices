package com.walkgs.crystolnetwork.offices.listeners;

import com.walkgs.crystolnetwork.offices.api.ServerOffices;
import com.walkgs.crystolnetwork.offices.events.PlayerUnInjectPermissibleEvent;
import com.walkgs.crystolnetwork.offices.inject.CrystolPermissible;
import com.walkgs.crystolnetwork.offices.inject.PermissibleInjector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissibleBase;

import java.util.UUID;

public final class InjectListener implements Listener {

    private final ServerOffices serverOffices = ServerOffices.getInstance();

    @EventHandler
    public final void onJoin(final PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        serverOffices.loadUser(uuid);
        serverOffices.injectInUser(player);

    }

    //@EventHandler
    public final void onQuit(final PlayerQuitEvent event) {

        final Player player = event.getPlayer();

        //TODO: GET CUSTOM PERMISSIBLE AND CALL EVENT
        final PermissibleBase injectedPermissibleBase = PermissibleInjector.getPermissible(player);
        if (injectedPermissibleBase != null) {

            CrystolPermissible permissibleBase = (CrystolPermissible) injectedPermissibleBase;

            final PlayerUnInjectPermissibleEvent permissibleEvent = new PlayerUnInjectPermissibleEvent(player, permissibleBase.getOldPermissibleBase(), permissibleBase).call();
            if (!permissibleEvent.isCancelled()) {
                //TODO: UNINJECT PERMISSIBLE
                PermissibleInjector.uninject(player);
            }
        }

    }

}
