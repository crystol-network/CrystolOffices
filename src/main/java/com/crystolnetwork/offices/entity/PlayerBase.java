package com.crystolnetwork.offices.entity;

import com.crystolnetwork.offices.services.OfficesServices;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public final class PlayerBase {

    private final OfficesServices officesServices;

    public PlayerBase(final OfficesServices officesServices) {
        this.officesServices = officesServices;
    }

    public PlayerPermission getUser(OfflinePlayer player) {
        return new PlayerPermission(player.getUniqueId(), officesServices);
    }

    public PlayerPermission getUser(UUID uuid) {
        return new PlayerPermission(uuid, officesServices);
    }

}
