package com.crystolnetwork.offices.services;

import com.crystolnetwork.offices.annotations.Singleton;
import com.crystolnetwork.offices.interfaces.ClickAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public final class JsonService implements Listener {
    private final OfficesServices officesServices = SingletonService.getOrFill(OfficesServices.class);
    private final Map<UUID, Map<String, ClickAction>> actions = new LinkedHashMap<>();

    public JsonService() {
        final Plugin plugin = officesServices.getPlugin();
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(this, plugin);
    }

    public void register(final UUID uuid, final String name, final ClickAction clickAction) {
        if (!this.actions.containsKey(uuid)) {
            this.actions.put(uuid, new LinkedHashMap<>());
        }
        this.actions.get(uuid).put(name, clickAction);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        this.actions.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onCommandSender(final PlayerCommandPreprocessEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        if (this.actions.containsKey(uuid)) {
            final String actionName = event.getMessage().split(" ")[0].substring(1);
            final Map<String, ClickAction> actionMap = this.actions.get(uuid);
            if (actionMap.containsKey(actionName)) {
                final ClickAction action = actionMap.get(actionName);
                action.run();
                if (action.expire) {
                    actionMap.remove(actionName);
                }
                event.setCancelled(true);
            }
        }
    }

}