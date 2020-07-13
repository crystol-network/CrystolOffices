package com.walkgs.crystolnetwork.offices;

import com.walkgs.crystolnetwork.offices.api.ServerOffices;
import com.walkgs.crystolnetwork.offices.listeners.InjectListener;
import com.walkgs.crystolnetwork.offices.services.GroupPermission;
import com.walkgs.crystolnetwork.offices.services.TabService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class OfficesPlugin extends JavaPlugin {

    //TODO: INSTANCE OF SERVICES OFFICES
    private ServerOffices serverOffices;

    @Override
    public void onEnable() {

        //TODO: INSTANCE OF SERVICES OFFICES
        serverOffices = ServerOffices.getInstance();

        //TODO: LOAD REGISTRED YML GROUPS
        serverOffices.getGroupLoader().loadGroups();

        //TODO: REGISTER EVENTS
        Bukkit.getPluginManager().registerEvents(new InjectListener(), this);

        //TODO: LOAD USER DATA AND INJECT CUSTOM PERMISSIBLE
        for (Player player : Bukkit.getOnlinePlayers()) {

            serverOffices.loadUser(player);

        }

        //TODO: STARTER CUSTOM TAB
        final TabService tabService = serverOffices.getTabService();
        tabService.start(this, getServer());
        tabService.execute(new TabService.TabUpdate() {

            @Override
            public void onUpdate(TabService.TabFactory tabFactory) {
                final Player player = tabFactory.getPlayer();
                final GroupPermission groupPermission = serverOffices.getUser(player).getLargestGroup();
                tabFactory.appendPrefix(groupPermission.getPrefix());
                tabFactory.appendSuffix(groupPermission.getSuffix());
            }

        });

    }
}
